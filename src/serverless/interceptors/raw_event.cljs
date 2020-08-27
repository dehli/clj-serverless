(ns serverless.interceptors.raw-event)

(def assoc-raw-event
  {:name :serverless/assoc-raw-event
   :enter (fn [{:keys [request] :as context}]
            (assoc context :request {:serverless/raw-event request}))})
