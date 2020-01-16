(ns serverless.aws.dynamo-db-replication
  (:require [serverless.aws.dynamo-db :as ddb]
            [serverless.core.async :refer [go-try <? <<?]]))

(defn- primary-model? [image]
  (= (:pk image) (:sk image)))

(defn- query-dependent-rows [client {pk :pk}]
  (ddb/query client {:KeyConditionExpression "pk = :pk and sk != :pk"
                     :ExpressionAttributeValues {":pk" pk}}))

(defn- update-dependent-row [client src-image {sk :sk}]
  (ddb/put client {:Item (assoc src-image :sk sk)}))

(defn replicate-rows
  "This function sphould be executed from a DDB stream."
  [client event]
  (go-try
    (let [image (ddb/new-image event)]
      (when (and (ddb/modify? event) (primary-model? image))
        (->> (<? (query-dependent-rows client image))
             (map (partial update-dependent-row client image))
             <<?)))))
