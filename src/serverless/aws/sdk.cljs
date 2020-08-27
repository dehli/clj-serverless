(ns serverless.aws.sdk
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [serverless.core.async :refer [go-try]]))

(def AWS (js/require "aws-sdk"))

(defn- ->js-params [params]
  (->> params
       (map (fn [[k v]]
              [(csk/->PascalCase k)
               v]))
       (into {})
       ->js))

(defn call [service method params]
  (let [promise (-> service
                    (j/call (csk/->camelCase method) (->js-params params))
                    (j/call :promise))]
    (go-try
      (cske/transform-keys csk/->kebab-case (->clj (<p! promise))))))

(defn- ->js-options [options]
  (->> options
       (map (fn [[k v]]
              [(csk/->camelCase k)
               (cske/transform-keys csk/->PascalCase v)]))
       (into {})
       ->js))

(defn service
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS (map csk/->PascalCase keys)])]
     (new Service (->js-options options)))))
