(ns serverless.aws.dynamo-db
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS]]))

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
