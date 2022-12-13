(ns serverless.interop-test
  (:require [cljs.test :as t]
            [cljs-bean.core :refer [->js]]
            [serverless.interop :as sut]))

(t/deftest ->clj-kebab-case
  (t/is (= (sut/->clj-kebab-case #js {"camelCaseKey" "testing"})
           {:camel-case-key "testing"}))

  (t/is (sut/->clj-kebab-case #js {"with/namespace" true}))
  (t/is (sut/->clj-kebab-case #js {"with/multiple/namespaces" true}))
  (t/is (sut/->clj-kebab-case #js {"/" true}))
  (t/is (sut/->clj-kebab-case #js {"" true})))
