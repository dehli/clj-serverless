(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]
            [serverless.env :refer [env->hash-map]]))

(def assoc-event
  {:name :assoc-event
   :enter (fn [event] {:event event})
   :leave (fn [_] {:statusCode 200})})

(def assoc-env
  {:name :assoc-env
   :enter #(assoc % :env (env->hash-map))})

(def merge-web-socket-deps
  {:name :merge-web-socket-deps
   :enter (fn [{:keys [event] :as context}]
            (update context :deps merge (api/ws-event->deps event)))})

(def ws-interceptors
  [assoc-event
   assoc-env
   merge-web-socket-deps])
