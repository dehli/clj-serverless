(ns serverless.env
  (:require [goog.object :as gobj]))

(defn env [key]
  (gobj/getValueByKeys js/process "env" key))

(defonce aws-lambda-function-name
  (env "AWS_LAMBDA_FUNCTION_NAME"))

(defonce aws-region
  (env "AWS_REGION"))
