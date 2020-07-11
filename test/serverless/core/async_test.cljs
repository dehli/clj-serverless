(ns serverless.core.async-test
  (:require [cljs.core.async :refer [go]]
            [cljs.test :refer [async deftest is]]
            [serverless.core.async :refer [<? <<? go-try]]))

(defonce ^:private error-channel
  (go (js/Error "my-error")))

(deftest throws-on-error
  (async done
    (go-try
      (<? error-channel)
      (is false)
      (catch :default e
        (is (= (.-message e) "my-error"))
        (done)))))

(deftest array-of-channels
  (async done
    (go-try
      (is (= (<<? [(go "a")
                   (go "b")
                   (go "c")])

             ["a" "b" "c"]))

      (done))))

(deftest array-of-channels-with-error
  (async done
    (go-try
      (<<? [(go "a")
            (go (js/Error. "my-error"))
            (go "c")])

      (catch :default e
        (is (= (.-message e) "my-error"))
        (done)))))
