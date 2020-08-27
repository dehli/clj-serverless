(ns serverless.json-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.json :as sut]))
(deftest clj<->json
  (let [clj-obj {:a "a" :b 0 :c true}
        json-str "{\"a\":\"a\",\"b\":0,\"c\":true}"]
    (is (= (sut/clj->json clj-obj) json-str))
    (is (= (sut/json->clj json-str) clj-obj))))
