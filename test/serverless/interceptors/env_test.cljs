(ns serverless.interceptors.env-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.interceptors.env :as sut]
            [serverless.interceptors.test-utils :as tu]))

(deftest assoc-env-with-mock-raw-env
  (let [ctx (tu/execute-with-context {::sut/raw-env #js {"AWS_REGION" "us-east-1" "FN_NAME" "test"}}
                                     [sut/assoc-env])]
    (is (= (::sut/env ctx)
           {:aws-region "us-east-1" :fn-name "test"}))))

(deftest assoc-env-with-actual-env
  (let [ctx (tu/execute-with-context {} [sut/assoc-raw-env sut/assoc-env])]
    (is (map? (::sut/env ctx)))))
