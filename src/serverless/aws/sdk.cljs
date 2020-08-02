(ns serverless.aws.sdk
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs.core.async.interop :refer-macros [<p!]]
            [cljs.core.async :refer [promise-chan put! close!]]
            [serverless.case-styles :refer [kebab->pascal]]
            [serverless.core.async :refer [go-try]]))

(def AWS (js/require "aws-sdk"))

(defn- callback [channel]
  (fn [error response]
    (cond
      (some? error)    (put! channel error)
      (some? response) (put! channel (js->clj response :keywordize-keys true))
      :else            (close! channel))))

(defn ^:deprecated js-call [client method args]
  (let [c (promise-chan)]
    (js-invoke client method (clj->js args) (callback c))
    c))

(defn ^:deprecated js-call-v2 [method xform client args]
  (let [c (promise-chan)]
    (js-invoke client method
               (clj->js (kebab->pascal (xform args)))
               (callback c))
    c))

(defn- transform-call-params [params]
  (->> params
       (map (fn [[k v]]
              [(csk/->PascalCase k)
               v]))
       (into {})))

(defn call [service {:keys [method params]}]
  (let [promise
        (-> service
            (j/call (csk/->camelCase method)
                    (clj->js (transform-call-params params)))
            (j/call :promise))]
    (go-try
      (cske/transform-keys
       csk/->kebab-case
       (js->clj (<p! promise) :keywordize-keys true)))))

(defn- transform-service-options [options]
  (->> options
       (map (fn [[k v]]
              [(csk/->camelCase k)
               (cske/transform-keys csk/->PascalCase v)]))
       (into {})))

(defn service
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS (map csk/->PascalCase keys)])]
     (new Service (clj->js (transform-service-options options))))))
