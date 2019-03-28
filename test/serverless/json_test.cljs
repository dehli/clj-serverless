(ns serverless.json-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.json :as sut]))

(deftest clj<->json
  (let [x {:a "a" :b 0 :c true}]
    (is (= (sut/clj->json x) "{\"a\":\"a\",\"b\":0,\"c\":true}"))
    (is (= (sut/json->clj (sut/clj->json x) :keywordize-keys true) x))))
