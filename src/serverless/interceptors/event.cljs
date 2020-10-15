(ns serverless.interceptors.event
  (:require [cljs-bean.core :refer [->js]]
            [serverless.interop :refer [->clj-kebab-case]]))

(def assoc-raw-event
  {:name ::assoc-raw-event
   :enter #(assoc % ::raw-event (:request %))})

(def assoc-event
  {:name ::assoc-event
   :enter (fn [{:keys [::raw-event] :as ctx}]
            (assoc ctx ::event (->clj-kebab-case raw-event)))
   :leave #(update % :response ->js)})
