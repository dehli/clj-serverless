(ns serverless.interceptors.use-cases
  (:require [serverless.interceptors.event :as event]
            [sieppari.context :refer [inject]]))

(def base-interceptors
  [event/assoc-raw-event
   event/assoc-event])

(def base
  {:name ::base :enter #(inject % base-interceptors)})
