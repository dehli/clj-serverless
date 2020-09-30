(ns serverless.interceptors.use-cases
  (:require [serverless.interceptors.env :as env]
            [serverless.interceptors.event :as event]
            [serverless.interceptors.now :refer [assoc-now]]
            [sieppari.context :refer [inject]]))

(def base-interceptors
  [event/assoc-raw-event
   event/assoc-event
   env/assoc-raw-env
   env/assoc-env
   assoc-now])

(def base
  {:name ::base :enter #(inject % base-interceptors)})
