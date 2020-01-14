(ns serverless.aws.dynamo-db-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.aws.dynamo-db :as sut]))

(deftest unmarshall
  (is (= (#'sut/unmarshall (clj->js {:hello {:S "there"}}))
         {:hello "there"})))
