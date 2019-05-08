(ns serverless.aws.lambda
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]))

(defonce ^:private Lambda (gobj/get AWS "Lambda"))

(defonce ^:private lambda (Lambda.))

(defn invoke [params]
  (js-call lambda "invoke" params))
