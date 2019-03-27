(ns serverless.aws.core
  (:require [cljs.core.async :refer [go <!]]
            [goog.object :as gobj]))

(defonce ^:private AWS (js/require "aws-sdk"))

(defn- clj->js-handler
  [handler]
  #(js/Promise.
    (fn [resolve reject]
      (go (let [x (-> %
                      (js->clj :keywordize-keys true)
                      handler
                      <!)]

            (if (instance? js/Error x)
              (reject (clj->js x))
              (resolve (clj->js x))))))))

(defn deflambda [key handler]
  (->> handler
       clj->js-handler
       (gobj/set js/exports (name key))))
