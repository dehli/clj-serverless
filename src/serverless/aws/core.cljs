(ns serverless.aws.core
  (:require [cljs.core.async :refer [go <!]]
            [goog.object :as gobj]
            [serverless.core.async :refer [channel?]]
            [serverless.json :refer [keyword->str]]))

(defn export-handler!
  [key handler]
  (gobj/set js/exports (name key) handler))

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
  (export-handler! key (partial js-handler clj-handler)))
