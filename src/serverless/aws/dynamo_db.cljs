(ns serverless.aws.dynamo-db
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]])
  (:refer-clojure :exclude [get update]))

(defonce ^:private DynamoDB (gobj/get AWS "DynamoDB"))
(defonce ^:private DocumentClient (gobj/get DynamoDB "DocumentClient"))
(defonce ^:private Converter (gobj/get DynamoDB "Converter"))

(defn- unmarshall [item]
  (-> (clj->js item)
      Converter.unmarshall
      (js->clj :keywordize-keys true)))

;; Streams (assumes batch size = 1)

(defonce ^:private record (comp first :Records))

(defn- event-name? [type record]
  (= (:eventName record) type))

(defonce insert? (comp (partial event-name? "INSERT") record))
(defonce modify? (comp (partial event-name? "MODIFY") record))
(defonce remove? (comp (partial event-name? "REMOVE") record))

(defonce old-image (comp unmarshall :OldImage :dynamodb record))
(defonce new-image (comp unmarshall :NewImage :dynamodb record))

(defn document-client [table-name]
  (-> {:params {:TableName table-name}}
      clj->js
      DocumentClient.))

(defn create-set [client values]
  (js-invoke client "createSet" (clj->js values)))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transaction Actions
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- transaction-action [key table-name options]
  {key (merge {:TableName table-name} options)})

(defn condition-check-action [table-name options]
  (transaction-action :ConditionCheck table-name options))

(defn delete-action [table-name options]
  (transaction-action :Delete table-name options))

(defn put-action [table-name options]
  (transaction-action :Put table-name options))

(defn update-action [table-name options]
  (transaction-action :Update table-name options))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Regular Actions
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn delete [client params]
  (js-call client "delete" params))

(defn get [client params]
  (js-call client "get" params))

(defn put [client params]
  (js-call client "put" params))

(defn query [client params]
  (js-call client "query" params))

(defn transact-write [client actions]
  (js-call client "transactWrite" {:TransactItems actions}))

(defn update [client params]
  (js-call client "update" params))
