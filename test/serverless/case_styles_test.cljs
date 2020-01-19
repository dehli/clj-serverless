(ns serverless.case-styles-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.case-styles :as sut]))

(deftest kebab->pascal
  (is (= (sut/kebab->pascal {:connection-id "abc"
                             :data {:hello "there"}})
         {:ConnectionId "abc"
          :Data {:hello "there"}})))
