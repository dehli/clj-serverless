(ns serverless.interceptors.now
  (:require [tick.alpha.api :as t]))

(def assoc-now
  {:name :serverless/assoc-now
   :enter (fn [context]
            (assoc context :serverless/now (t/now)))})
