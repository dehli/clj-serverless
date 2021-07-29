(ns serverless.interceptors.use-cases
  (:require [serverless.interceptors.env :as env]
            [serverless.interceptors.event :as event]
            [sieppari.context :refer [inject]]))

(def base-interceptors
  [event/assoc-raw-event
   event/assoc-event
   env/assoc-raw-env
   env/assoc-env])

(def base
  {:name ::base :enter #(inject % base-interceptors)})
