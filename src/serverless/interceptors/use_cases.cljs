(ns serverless.interceptors.use-cases
  (:require [serverless.interceptors.env :refer [assoc-env]]
            [serverless.interceptors.event :refer [assoc-event]]
            [serverless.interceptors.now :refer [assoc-now]]
            [sieppari.context :refer [inject]]))

(def base
  {:name ::base
   :enter #(inject % [assoc-event assoc-env assoc-now])})
