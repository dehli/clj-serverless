(ns serverless.aws.sdk
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->js]]
            [serverless.interop :refer [promise->chan]]))

(def AWS (js/require "aws-sdk"))

(defn- ->PascalCase [params]
  (cske/transform-keys csk/->PascalCase params))

(defn call
  [service method params]
  (-> service
      (j/call (csk/->camelCase method) (->js (->PascalCase params)))
      (j/call :promise)
      promise->chan))

(defn- format-options
  [options]
  (->> options
       (map (fn [[k v]] [(csk/->camelCase k) (->PascalCase v)]))
       (into {})))

(defn service
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS (map csk/->PascalCase keys)])]
     (new Service (->js (format-options options))))))
