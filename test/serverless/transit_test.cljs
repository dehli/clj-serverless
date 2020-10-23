(ns serverless.transit-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.transit :as sut]
            [tick.alpha.api :as time]))

(deftest writes-instants
  (let [timestamp (time/now)]
    (is (= (-> timestamp sut/write sut/read)
           timestamp))))
