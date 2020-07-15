(ns serverless.aws.core
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [cljs.core.async.interop :refer-macros [<p!]]
            [cljs.core.async :refer [go <!]]
            [goog.object :as gobj]
            [serverless.core.async :refer [channel? go-try]]
            [serverless.json :refer [keyword->str]]))

(def AWS (js/require "aws-sdk"))

(defn service
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS keys])]
     (new Service (clj->js options)))))

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

(defn call [service action args]
  (let [promise (-> service
                    (j/call (csk/->camelCase action) (clj->js args))
                    (j/call :promise))]
    (go-try
      (js->clj (<p! promise) :keywordize-keys true))))
