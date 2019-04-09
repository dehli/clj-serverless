(ns serverless.math-test
  (:require [cljs.test :refer [deftest is]]
            [serverless.math :as sut]))

(deftest degrees->radians
  (is (= (sut/degrees->radians 0)
         0))
  (is (= (sut/degrees->radians 90)
         (* 0.5 js/Math.PI)))
  (is (= (sut/degrees->radians 180)
         js/Math.PI))
  (is (= (sut/degrees->radians 270)
         (* 1.5 js/Math.PI)))
  (is (= (sut/degrees->radians 360)
         (* 2 js/Math.PI))))

(deftest radians->degrees
  (is (= (sut/radians->degrees 0)
         0))
  (is (= (sut/radians->degrees (* 0.5 js/Math.PI))
         90))
  (is (= (sut/radians->degrees js/Math.PI)
         180))
  (is (= (sut/radians->degrees (* 1.5 js/Math.PI))
         270))
  (is (= (sut/radians->degrees (* 2 js/Math.PI))
         360)))
