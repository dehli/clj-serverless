(ns serverless.aws.dynamo-db
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call js-call-v2]]
            [serverless.core :refer [def-]])
  (:refer-clojure :exclude [get update]))

(def- DynamoDB (gobj/get AWS "DynamoDB"))
(def- DocumentClient (gobj/get DynamoDB "DocumentClient"))
(def- Converter (gobj/get DynamoDB "Converter"))

(def unmarshall
  (let [unmarshall (gobj/get Converter "unmarshall")]
    (fn [item] (-> item clj->js  unmarshall (js->clj :keywordize-keys true)))))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Streams (assumes batch size = 1)
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def- record (comp first :Records))

(defn- event-name? [type record]
  (= (:eventName record) type))

(def insert? (comp (partial event-name? "INSERT") record))
(def modify? (comp (partial event-name? "MODIFY") record))
(def remove? (comp (partial event-name? "REMOVE") record))

(def old-image (comp unmarshall :OldImage :dynamodb record))
(def new-image (comp unmarshall :NewImage :dynamodb record))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Document Clients
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def- default-client
  (new DocumentClient))

(defn document-client [table-name]
  (new DocumentClient (clj->js {:params {:TableName table-name}})))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Set Code
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-set
  ([values] (create-set default-client values))
  ([client values]
   (js-invoke client "createSet" (clj->js values))))

(defn set->clj-set [s]
  (set (gobj/get s "values")))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transaction Actions
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- transaction-action [key table-name options]
  {key (merge {:TableName table-name} options)})

(def condition-check-action (partial transaction-action :ConditionCheck))
(def delete-action (partial transaction-action :Delete))
(def put-action (partial transaction-action :Put))
(def update-action (partial transaction-action :Update))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Regular Actions
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def delete
  (partial js-call-v2 "delete" identity))

(def get
  (partial js-call-v2 "get" identity))

(def put
  (partial js-call-v2 "put" identity))

(def query
  (partial js-call-v2 "query" identity))

(defn transact-write [client actions]
  (js-call client "transactWrite" {:TransactItems actions}))

(def update
  (partial js-call-v2 "update" identity))

;; Used to generate dependencies for interceptors
(defn table-name->deps [table-name]
  (let [client (document-client table-name)]
    #:dynamo-db
    {;; Transaction methods
     :condition-check-action (partial condition-check-action table-name)
     :delete-action (partial delete-action table-name)
     :put-action (partial put-action table-name)
     :update-action (partial update-action table-name)
     ;; Regular methods
     :delete (partial delete client)
     :get (partial get client)
     :put (partial put client)
     :query (partial query client)
     :transact-write (partial transact-write client)
     :update (partial update client)}))
