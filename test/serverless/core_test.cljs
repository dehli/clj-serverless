(ns serverless.core-test
  (:require [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.core :as sut]))

(defonce ^:private error-channel
  (go (js/Error "my-error")))

(deftest async-macros
  (async done
    (sut/go-try
      (sut/<? error-channel)
      (is false)
      (catch :default e
        (is (= (.-message e) "my-error"))
        (done)))))
