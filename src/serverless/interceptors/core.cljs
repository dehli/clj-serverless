(ns serverless.interceptors.core
  (:require [sieppari.core :as s]
            [sieppari.async.core-async]))

(defn interceptors->handler
  ([interceptors]
   (interceptors->handler {} interceptors))
  ([{:keys [execute-context?]} interceptors]
   (fn [event]
     (let [execute (if execute-context?
                     s/execute-context
                     s/execute)

           execute-arg (if execute-context?
                         {:request event}
                         event)]

       (js/Promise. (fn [resolve reject]
                      (execute (into [] interceptors)
                               execute-arg
                               #(resolve (cond-> %
                                           execute-context?
                                           :response))
                               reject)))))))
