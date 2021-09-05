(ns serverless.interceptors.core-test
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [cljs.test :as t]
            [serverless.interceptors.core :as sut]))

(t/deftest interceptors->handler
  (t/async done
    (go
      (t/testing "default execute"
        (let [handler (sut/interceptors->handler
                       [{:enter #(assoc-in % [:request :a] 1)}
                        #(assoc % :b 2)])]

          (t/is (= (<p! (handler {:c 3}))
                   {:a 1 :b 2 :c 3}))))

      (t/testing "execute-context?"
        (let [handler (sut/interceptors->handler
                       {:execute-context? true}
                       [{:enter #(assoc-in % [:request :a] 1)}
                        {:leave #(assoc % :response (merge (:request %)
                                                           {:b 2}))}])]

          (t/is (= (<p! (handler {:c 3}))
                   {:a 1 :b 2 :c 3}))))

      (done))))
