(ns serverless.interceptors.raw-event)

(def assoc-raw-event
  {:name :serverless/assoc-raw-event
   :enter #(assoc % :serverless/raw-event (:request %))})
