(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS]]
            [serverless.json :refer [json->clj]]))

(defonce ^:private ManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

(defn management-api [endpoint]
  (-> {:apiVersion "2018-11-29" :endpoint endpoint}
      clj->js
      ManagementApi.))

(defonce request-context :requestContext)
(defonce connection-id (comp :connectionId request-context))
(defonce authorizer (comp :authorizer request-context))
(defonce sub (comp :principalId authorizer))
(defonce body #(-> % :body (json->clj :keywordize-keys true)))
(defonce endpoint (comp #(str (:domainName %) "/" (:stage %)) request-context))
