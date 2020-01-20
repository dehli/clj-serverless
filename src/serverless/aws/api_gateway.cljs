(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call-v2]]
            [serverless.core :refer [def-]]
            [serverless.json :refer [json->clj clj->json]]))

(def- ApiGatewayManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

;; Protocol
(defprotocol ManagementApiProtocol
  "A protocol to interact with ApiGatewayManagementApi"
  (delete-connection [this params] "Delete a connection")
  (get-connection [this params] "Get a connection")
  (post-to-connection [this params] "Post data to a connection"))

(deftype ManagementApi [api]
  ManagementApiProtocol
  (delete-connection [_ params]
    (js-call-v2 api "deleteConnection" params))
  (get-connection [_ params]
    (js-call-v2 api "getConnection" params))
  (post-to-connection [_ params]
    (js-call-v2 api "postToConnection" (update params :data clj->json))))

(defn management-api [^String endpoint]
  (->> (clj->js {:apiVersion "2018-11-29" :endpoint endpoint})
       (new ApiGatewayManagementApi)
       ->ManagementApi))

;; Accessors
(def request-context :requestContext)
(def connection-id (comp :connectionId request-context))
(def authorizer (comp :authorizer request-context))
(def sub (comp :principalId authorizer))
(def body #(-> % :body (json->clj :keywordize-keys true)))
(def endpoint (comp #(str (:domainName %) "/" (:stage %)) request-context))
