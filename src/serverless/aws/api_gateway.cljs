(ns serverless.aws.api-gateway
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS]]))

(defonce ^:private ManagementApi (gobj/get AWS "ApiGatewayManagementApi"))

(defn management-api [endpoint]
  (-> {:apiVersion "2018-11-29" :endpoint endpoint}
      clj->js
      ManagementApi.))
