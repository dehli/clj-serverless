(ns serverless.aws.sdk
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [serverless.core.async :refer [go-try]]))

(def AWS (js/require "aws-sdk"))

(defn- ->PascalCase [params]
  (cske/transform-keys csk/->PascalCase params))

(defn call [service method params]
  (let [promise (-> service
                    (j/call (csk/->camelCase method) (->js (->PascalCase params)))
                    (j/call :promise))]
    (go-try
      (cske/transform-keys csk/->kebab-case (->clj (<p! promise))))))

(defn- format-options [options]
  (->> options
       (map (fn [[k v]] [(csk/->camelCase k) (->PascalCase v)]))
       (into {})))

(defn service
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS (map csk/->PascalCase keys)])]
     (new Service (->js (format-options options))))))
