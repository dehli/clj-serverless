(ns serverless.interceptors.core
  (:require [sieppari.core :as s]
            [sieppari.async.core-async]))

(defn interceptors->handler [interceptors]
  (fn [event]
    (js/Promise. #(s/execute (into [] interceptors) event %1 %2))))
