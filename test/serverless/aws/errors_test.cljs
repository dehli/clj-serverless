(ns serverless.aws.errors-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.aws.errors :as sut])
  (:refer-clojure :exclude [name]))

(deftest name
  (is (= (sut/name #js {:name "ValidationException"})
         :validation-exception)))
