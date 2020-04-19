(ns serverless.aws.s3
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]))

(def ^:private S3 (gobj/get AWS "S3"))

(defn s3
  ([] (s3 {}))
  ([options] (new S3 (clj->js options))))

(defn bucket->s3 [bucket]
  (s3 {:params {:Bucket bucket}}))

;; Signed url methods
(defn- signed-url [operation client params]
  (js-invoke client "getSignedUrl" operation params))
(def get-object-signed-url (partial signed-url "getObject"))
(def put-object-signed-url (partial signed-url "putObject"))
