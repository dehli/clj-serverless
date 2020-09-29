(ns serverless.interceptors.test-utils
  (:require [sieppari.core :as s]))

(defn execute-with-context
  "Allows you to test a series of interceptor and pass in mocked context objects."
  [ctx interceptors]
  (let [success (atom nil)]
    (s/execute (concat [{:enter #(merge % ctx) :leave #(assoc % :response %)}]
                       interceptors)
               nil
               #(reset! success %)
               identity)
    @success))
