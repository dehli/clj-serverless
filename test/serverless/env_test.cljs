(ns serverless.env-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.env :as sut]))

(deftest env->hash-map
  (is (= (sut/env->hash-map #js {"AWS_REGION" "us-east-1" "FN_NAME" "test"})
         {:aws-region "us-east-1" :fn-name "test"})))
