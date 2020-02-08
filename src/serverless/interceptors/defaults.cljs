(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]
            [serverless.aws.dynamo-db :as ddb]
            [serverless.env :refer [env->hash-map]]))

(def assoc-raw-event
  {:name :assoc-raw-event
   :enter (fn [event] {:raw-event event})})

(def assoc-ws-event
  {:name :assoc-ws-event
   :enter (fn [{:keys [raw-event] :as ctx}]
            (let [sub (api/sub raw-event)]
              (assoc ctx :event
                     {:body (try (api/body raw-event) (catch :default e nil))
                      :connection-id (api/connection-id raw-event)
                      :route (api/route-key raw-event)
                      :sub (when (not= sub "anonymous") sub)})))
   :leave (fn [_] {:statusCode 200})})

(def assoc-env
  {:name :assoc-env
   :enter #(assoc % :env (env->hash-map))})

(def merge-dynamo-db-deps
  {:name :merge-dynamo-db-deps
   :enter (fn [{:keys [env] :as context}]
            (update context :deps merge
                    (ddb/table-name->deps (:table-name env))))})

(def merge-web-socket-deps
  {:name :merge-web-socket-deps
   :enter (fn [{:keys [raw-event] :as context}]
            (update context :deps merge
                    (api/ws-event->deps raw-event)))})

(def ws-interceptors
  [assoc-raw-event
   assoc-ws-event
   assoc-env
   merge-dynamo-db-deps
   merge-web-socket-deps])
