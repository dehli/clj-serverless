(ns serverless.interceptors.event
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]))

(def assoc-raw-event
  {:name ::assoc-raw-event
   :enter #(assoc % ::raw-event (:request %))})

(def assoc-event
  {:name ::assoc-event
   :enter (fn [{:keys [::raw-event] :as ctx}]
            (assoc ctx ::event (cske/transform-keys csk/->kebab-case (->clj raw-event))))

   :leave #(update % :response ->js)})
