(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call-v2]]
            [serverless.core :refer [def-]]
            [serverless.json :refer [json->clj clj->json]]))

(def- ApiGatewayManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

;; Constructor
(defn api-gateway-management-api [^String endpoint]
  (new ApiGatewayManagementApi
       (clj->js {:apiVersion "2018-11-29" :endpoint endpoint})))

;; Public methods
(defn delete-connection [api params]
  (js-call-v2 api "deleteConnection" params))
(defn get-connection [api params]
  (js-call-v2 api "getConnection" params))
(defn post-to-connection [api params]
  (js-call-v2 api "postToConnection" (update params :data clj->json)))

;; Accessors
(def request-context :requestContext)
(def connection-id (comp :connectionId request-context))
(def authorizer (comp :authorizer request-context))
(def sub (comp :principalId authorizer))
(def body #(-> % :body (json->clj :keywordize-keys true)))
(def endpoint (comp #(str (:domainName %) "/" (:stage %)) request-context))
