(ns serverless.aws.ses
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]))

(defonce ^:private SES (gobj/get AWS "SES"))

(defonce ^:private ses
  (-> {:apiVersion "2010-12-01"}
      clj->js
      SES.))

(defonce send-email (partial js-call ses "sendEmail"))
