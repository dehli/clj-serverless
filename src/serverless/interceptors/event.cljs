(ns serverless.interceptors.event
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]))

(def assoc-event
  {:name :serverless/assoc-event
   :enter (fn [{:keys [serverless/raw-event] :as context}]
            (assoc context
                   :serverless/event
                   (cske/transform-keys csk/->kebab-case (->clj raw-event))))

   :leave (fn [context]
            (update context :response ->js))})
