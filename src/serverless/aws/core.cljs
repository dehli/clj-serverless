(ns serverless.aws.core
  (:require [cljs.core.async :refer [go <!]]
            [goog.object :as gobj]
            [serverless.core.async :refer [channel?]]
            [serverless.json :refer [keyword->str]]))

(defn- js-handler
  [clj-handler event]
  (js/Promise.
   (fn [resolve reject]
     (go
       (let [result-or-chan (-> event (js->clj :keywordize-keys true) clj-handler)
             result (if (channel? result-or-chan)
                      (<! result-or-chan)
                      result-or-chan)]

         (if (instance? js/Error result)
           (reject result)
           (resolve (clj->js result :keyword-fn keyword->str))))))))

(defn deflambda [key clj-handler]
  (->> clj-handler
       (partial js-handler)
       (gobj/set js/exports (name key))))
