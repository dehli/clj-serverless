(ns serverless.interceptors.now
  (:require [tick.alpha.api :as t]))

(def assoc-now
  {:name ::assoc-now
   :enter #(assoc % ::now (t/now))})
