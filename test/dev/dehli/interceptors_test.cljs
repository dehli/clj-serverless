(ns dev.dehli.interceptors-test
  (:require [applied-science.js-interop :as j]
            [cljs.test :as t]
            [dev.dehli.interceptors :as sut]
            [serverless.interceptors.test-utils :as tu]))

(t/deftest merge-system-env
  (t/testing "handles valid process.env"
    (let [ctx (tu/execute-with-context {} [sut/merge-system-env])]
      (t/is (= (:system.env/pwd ctx)
               (j/get-in js/process [:env :PWD]))))))
