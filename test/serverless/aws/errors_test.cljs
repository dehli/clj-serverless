(ns serverless.aws.errors-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.aws.errors :as sut]))

(deftest name
  (is (= (sut/name #js {:name "ValidationException"})
         :validation-exception)))
