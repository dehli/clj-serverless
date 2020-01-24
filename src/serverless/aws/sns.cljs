(ns serverless.aws.sns
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call-v2]]
            [serverless.core :refer [def-]]))

(def- SNS (gobj/get AWS "SNS"))

;; Constructor
(defn service
  [^String topic-arn]
  (new SNS (clj->js {:apiVersion "2010-03-31"
                     :params {:TopicArn topic-arn}})))

;; Public methods
(def publish
  (partial js-call-v2 "publish" identity))
