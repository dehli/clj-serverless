(ns serverless.interceptors.core
  (:require [serverless.interceptors.env :refer [assoc-env]]
            [serverless.interceptors.event :refer [assoc-event]]
            [serverless.interceptors.now :refer [assoc-now]]
            [serverless.interceptors.raw-env :refer [assoc-raw-env]]
            [serverless.interceptors.raw-event :refer [assoc-raw-event]]
            [sieppari.core :as s]
            [sieppari.async.core-async]))

(defn interceptors->handler [interceptors]
  (fn [event]
    (js/Promise. #(s/execute (into [] interceptors) event %1 %2))))

(def common-interceptors
  [assoc-raw-event
   assoc-event
   assoc-raw-env
   assoc-env
   assoc-now])
