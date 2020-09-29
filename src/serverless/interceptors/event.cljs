(ns serverless.interceptors.event
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]
            [serverless.interceptors.core :refer [inject-deps]]))

(def assoc-raw-event
  {:name ::assoc-raw-event
   :enter #(assoc % ::raw-event (:request %))})

(def assoc-event
  {:name ::assoc-event
   :enter (fn [{:keys [::raw-event] :as ctx}]
            (if (nil? raw-event)
              (inject-deps ctx assoc-raw-event)
              (assoc ctx ::event (cske/transform-keys csk/->kebab-case (->clj raw-event)))))

   :leave #(update % :response ->js)})
