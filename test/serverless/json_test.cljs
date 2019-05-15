(ns serverless.json-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.json :as sut]))

(deftest keyword->str
  (is (= (sut/keyword->str :abc/def) "abc/def"))
  (is (= (sut/keyword->str :my-keyword) "my-keyword"))
  (is (= (sut/clj->json {:my/keyword true}) "{\"keyword\":true}"))
  (is (= (sut/clj->json {:my/keyword true} :keyword-fn sut/keyword->str)
         "{\"my/keyword\":true}")))

(deftest clj<->json
  (let [x {:a "a" :b 0 :c true}]
    (is (= (sut/clj->json x) "{\"a\":\"a\",\"b\":0,\"c\":true}"))
    (is (= (sut/json->clj (sut/clj->json x) :keywordize-keys true) x))))
