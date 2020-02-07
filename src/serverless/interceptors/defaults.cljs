(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]))

(def assoc-event
  {:name :move-event
   :enter (fn [event] {:event event})})

(def merge-web-socket-deps
  {:name :merge-web-socket-deps
   :enter (fn [{:keys [event] :as context}]
            (update context :deps merge (api/ws-event->deps event)))})

(def web-socket-default-interceptors
  [assoc-event
   merge-web-socket-deps])
