(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]))

(def assoc-event
  {:name :assoc-event
   :enter (fn [event] {:event event})
   :leave (fn [_] {:statusCode 200})})

(def merge-web-socket-deps
  {:name :merge-web-socket-deps
   :enter (fn [{:keys [event] :as context}]
            (update context :deps merge (api/ws-event->deps event)))})

(def ws-interceptors
  [assoc-event
   merge-web-socket-deps])
