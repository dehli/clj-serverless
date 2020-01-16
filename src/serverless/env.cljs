(ns serverless.env
  (:require [goog.object :as gobj]))

(defn env [key]
  (gobj/getValueByKeys js/process "env" key))

(def aws-lambda-function-name
  (env "AWS_LAMBDA_FUNCTION_NAME"))

(def aws-region
  (env "AWS_REGION"))

(def logging-level
  (env "LOGGING_LEVEL"))
