(ns serverless.interceptors.core-test
  (:require [cljs.test :refer [async deftest is]]
            [serverless.core.async :refer [go-try <!]]
            [serverless.interceptors.core :as sut]))

(deftest add-interceptors
  (async done
    (go-try
      (let [interceptors [{:name :a
                           :enter (fn [{:keys [test] :as ctx}]
                                    (-> ctx
                                        (assoc :a-enter test)
                                        (update :test inc)))
                           :leave (fn [{:keys [test] :as ctx}]
                                    (-> ctx
                                        (assoc :a-leave test)
                                        (update :test inc)))}
                          {:name :b
                           :enter (fn [{:keys [test] :as ctx}]
                                    (go-try
                                      (-> ctx
                                          (assoc :b-enter test)
                                          (update :test inc))))
                           :leave (fn [{:keys [test] :as ctx}]
                                    (-> ctx
                                        (assoc :b-leave test)
                                        (update :test inc)))}]

            handler (sut/add-interceptors interceptors
                                          #(assoc % :handler (:test %)))]

        (is (= (<! (handler {:event "test"}))
               {:event "test"
                :a-enter nil
                :b-enter 1
                :handler 2
                :b-leave 2
                :a-leave 3
                :test 4}))

        (done)))))
