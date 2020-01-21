(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call-v2]]
            [serverless.core :refer [def-]]
            [serverless.json :refer [json->clj clj->json]]))

(def- ManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

;; Constructor
(defn management-api [^String endpoint]
  (new ManagementApi (clj->js {:apiVersion "2018-11-29" :endpoint endpoint})))

;; Method helpers
(def- base-xform #(select-keys % [:connection-id]))
(defn- post-xform [p]
  (-> p (update :data clj->json) (select-keys [:connection-id :data])))

;; Public methods
(def delete-connection
  (partial js-call-v2 "deleteConnection" base-xform))
(def get-connection
  (partial js-call-v2 "getConnection" base-xform))
(def post-to-connection
  (partial js-call-v2 "postToConnection" post-xform))

;; Accessors
(def request-context :requestContext)
(def connection-id (comp :connectionId request-context))
(def authorizer (comp :authorizer request-context))
(def sub (comp :principalId authorizer))
(def body #(-> % :body (json->clj :keywordize-keys true)))
(def endpoint (comp #(str (:domainName %) "/" (:stage %)) request-context))
