(ns serverless.interceptors.core
  (:require [sieppari.core :as s]
            [sieppari.async.core-async]))

(defn add-interceptors [interceptors handler]
  (fn [event]
    (js/Promise. #(s/execute (conj interceptors handler) event %1 %2))))
