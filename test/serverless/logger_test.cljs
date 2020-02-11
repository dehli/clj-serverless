(ns serverless.logger-test
  (:require [cljs.test :refer [deftest is testing]]
            [serverless.logger :as sut]))

(deftest debug-logger
  (let [make-logger #(#'sut/make-logger {:env-level %
                                         :required-level "debug"
                                         :logger identity})]

    (testing "Logs when levels equal"
      (let [logger (make-logger "debug")]
        (is (true? (logger true)))))

    (testing "Doesn't log when current level is higher"
      (let [logger (make-logger "WARN")]
        (is (nil? (logger true)))))

    (testing "Doesn't log if env not set"
      (let [logger (make-logger nil)]
        (is (nil? (logger true)))))))
