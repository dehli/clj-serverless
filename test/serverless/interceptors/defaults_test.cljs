(ns serverless.interceptors.defaults-test
  (:require [cljs.test :refer [async deftest is testing]]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [serverless.interceptors.core :refer [add-interceptors]]
            [serverless.interceptors.defaults :as sut]))

(deftest assoc-ws-event
  (let [{:keys [enter]} sut/assoc-ws-event
        raw-event {:body "{\"hello\":\"there\"}"
                   :requestContext {:connectionId "conn1"
                                    :routeKey "$default"
                                    :authorizer {:principalId "my-sub"}}}

        expected {:body {:hello "there"}
                  :connection-id "conn1"
                  :route "$default"
                  :sub "my-sub"}

        is-expected (fn [raw-event expected]
                      (is (= (enter {:request {:raw-event raw-event}})
                             {:request {:raw-event raw-event
                                        :event expected}})))]

    (testing "valid request"
      (is-expected raw-event expected))

    (testing "anonymous token"
      (is-expected (assoc-in raw-event
                             [:requestContext :authorizer :principalId]
                             "anonymous")
                   (assoc expected :sub nil)))

    (testing "invalid JSON"
      (is-expected (assoc raw-event :body "{\"invalid")
                   (assoc expected :body nil)))))

(deftest websocket-interceptors
  (async done
    (go
      (let [handler (add-interceptors sut/ws-interceptors
                                      #(assoc % :handler true))]
        (is (= (js/JSON.stringify (<p! (handler {:hello "world"})))
               "{\"statusCode\":200}"))

        (done)))))
