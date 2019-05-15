(ns serverless.aws.core
  (:require [cljs.core.async :refer [go <!]]
            [goog.object :as gobj]
            [serverless.json :refer [keyword->str]]))

(defn- js-handler
  [clj-handler event]
  (js/Promise.
   (fn [resolve reject]
     (go
       (let [x (-> event (js->clj :keywordize-keys true) clj-handler <!)]
         (if (instance? js/Error x)
           (reject x)
           (resolve (clj->js x :keyword-fn keyword->str))))))))

(defn deflambda [key clj-handler]
  (->> clj-handler
       (partial js-handler)
       (gobj/set js/exports (name key))))
