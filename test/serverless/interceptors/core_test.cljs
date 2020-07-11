(ns serverless.interceptors.core-test
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [cljs.test :refer [async deftest is]]
            [serverless.interceptors.core :as sut]))

(deftest add-interceptors
  (async done
    (go
      (let [handler (sut/add-interceptors
                     [{:enter #(assoc-in % [:request :a] 1)}]
                     #(assoc % :b 2))]

        (is (= (<p! (handler {:c 3}))
               {:a 1 :b 2 :c 3}))

        (done)))))
