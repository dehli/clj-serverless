(ns serverless.aws.sdk
  (:require [cljs.core.async :refer [promise-chan put! close!]]))

(defonce AWS (js/require "aws-sdk"))

(defn js-call [client method args]
  (let [c (promise-chan)]
    (js-invoke client
               method
               (clj->js args)
               (fn [error response]
                 (cond
                   (some? error)
                   (put! c error)

                   (some? response)
                   (put! c (js->clj response :keywordize-keys true))

                   :else
                   (close! c))))
    c))
