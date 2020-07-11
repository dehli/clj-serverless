(ns serverless.interceptors.defaults
  (:require [serverless.aws.api-gateway :as api]
            [serverless.aws.dynamo-db :as ddb]
            [serverless.env :refer [env->hash-map]]
            [serverless.logger :as logger]))

(def js<->clj
  {:enter #(update % :request (js->clj % :keywordize-keys true))
   :leave #(update % :response clj->js)})

(def assoc-raw-event
  {:name :assoc-raw-event
   :enter (fn [{:keys [request] :as ctx}]
            (assoc ctx :request {:raw-event request}))})

(def assoc-ws-event
  {:name :assoc-ws-event
   :enter (fn [{:keys [request] :as ctx}]
            (let [raw-event (:raw-event request)
                  sub (api/sub raw-event)]
              (assoc-in ctx [:request :event]
                        {:body (try (api/body raw-event) (catch :default _ nil))
                         :connection-id (api/connection-id raw-event)
                         :route (api/route-key raw-event)
                         :sub (when (not= sub "anonymous") sub)})))
   :leave (fn [ctx] (assoc ctx :response {:statusCode 200}))})

(def assoc-env
  {:name :assoc-env
   :enter #(assoc-in % [:request :env] (env->hash-map))})

(def merge-logger-deps
  {:name :merge-logger-deps
   :enter #(update-in % [:request :deps] merge (logger/context->deps %))})

(def merge-dynamo-db-deps
  {:name :merge-dynamo-db-deps
   :enter (fn [ctx]
            (update-in ctx [:request :deps] merge
                       (ddb/table-name->deps (get-in ctx [:env :table-name]))))})

(def merge-web-socket-deps
  {:name :merge-web-socket-deps
   :enter (fn [{:keys [request] :as context}]
            (update-in context [:request :deps] merge
                       (api/ws-event->deps (:raw-event request))))})

(def ws-interceptors
  [js<->clj
   assoc-raw-event
   assoc-ws-event
   assoc-env
   merge-logger-deps
   merge-dynamo-db-deps
   merge-web-socket-deps])
