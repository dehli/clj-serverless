(ns serverless.interceptors.event
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]))

(def assoc-event
  {:name :serverless/assoc-event
   :enter (fn [{:keys [request] :as context}]
            (let [{:keys [serverless/raw-event]} request]
              (assoc-in context [:request :serverless/event]
                        (cske/transform-keys csk/->kebab-case (->clj raw-event)))))
   :leave (fn [context]
            (update context :response ->js))})
