(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]
            [serverless.json :refer [json->clj clj->json]]))

(defonce ^:private ManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

(defn management-api [endpoint]
  (new ManagementApi (clj->js {:apiVersion "2018-11-29" :endpoint endpoint})))

(defonce request-context :requestContext)
(defonce connection-id (comp :connectionId request-context))
(defonce authorizer (comp :authorizer request-context))
(defonce sub (comp :principalId authorizer))
(defonce body #(-> % :body (json->clj :keywordize-keys true)))
(defonce endpoint (comp #(str (:domainName %) "/" (:stage %)) request-context))

(defn post-to-connection [client {:keys [connection-id data]}]
  (js-call client
           "postToConnection"
           {:ConnectionId connection-id
            :Data (clj->json data)}))
