(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]
            [serverless.aws.dynamo-db :as ddb]
            [serverless.env :refer [env->hash-map]]))

(def assoc-event
  {:name :assoc-event
   :enter (fn [event] {:event event})
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
   :enter (fn [{:keys [event] :as context}]
            (update context :deps merge
                    (api/ws-event->deps event)))})

(def ws-interceptors
  [assoc-event
   assoc-env
   merge-dynamo-db-deps
   merge-web-socket-deps])
