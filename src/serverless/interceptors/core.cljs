(ns serverless.interceptors.core
  (:require [sieppari.core :as s]
            [sieppari.async.core-async]
            [sieppari.context :refer [inject]]))

(defn interceptors->handler [interceptors]
  (fn [event]
    (js/Promise. #(s/execute (into [] interceptors) event %1 %2))))

(defn inject-deps
  "This function can be used to inject interceptor dependencies.
  Note the current interceptor will re-execute."
  [ctx interceptor-or-interceptors]
  (let [current (.shift (:stack ctx))]
    (-> ctx
        (inject current)
        (inject interceptor-or-interceptors))))
