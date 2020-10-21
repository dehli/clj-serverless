(ns serverless.utils.map-test
  (:require [camel-snake-kebab.core :as csk]
            [cljs.test :refer [deftest is]]
            [serverless.utils.map :as sut]))

(deftest kv-transform-keywords
  (is (= (sut/kv-transform-keywords csk/->camelCase
                                    #(sut/transform-keywords csk/->PascalCase %)
                                    {:my-params {:table-name "test-table"}
                                     "TestKey" {:transform-me true
                                                "skipMe" false}})
         {:myParams {:TableName "test-table"}
          "TestKey" {:TransformMe true
                     "skipMe" false}})))

(deftest transform-keywords
  (is (= (sut/transform-keywords csk/->camelCase
                                 {:my-test {:a-b-c "def"}
                                  "KeepMe" {:foo-bar "baz"}})
         {:myTest {:aBC "def"}
          "KeepMe" {:fooBar "baz"}})))
