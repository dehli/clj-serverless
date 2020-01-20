(ns serverless.aws.sdk
  (:require [cljs.core.async :refer [promise-chan put! close!]]
            [serverless.case-styles :refer [kebab->pascal]]))

(def AWS (js/require "aws-sdk"))

(defn- callback [channel]
  (fn [error response]
    (cond
      (some? error)    (put! channel error)
      (some? response) (put! channel (js->clj response :keywordize-keys true))
      :else            (close! channel))))

(defn js-call [client method args]
  (let [c (promise-chan)]
    (js-invoke client method (clj->js args) (callback c))
    c))

(defn js-call-v2 [client method args]
  (let [c (promise-chan)]
    (js-invoke client method (clj->js (kebab->pascal args)) (callback c))
    c))
