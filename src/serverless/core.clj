(ns serverless.core
  (:require [cljs.analyzer.api :as api]))

(defmacro import-vars [[_ ns]]
  `(do
     ~@(map (fn [[k# _]]
              `(defonce ~(symbol k#)
                 ~(symbol (name ns) (name k#))))

            (api/ns-publics ns))))

(defmacro go-try
  [& body]
  `(let [chan# (cljs.core.async/promise-chan)]
     (cljs.core.async/go
       (let [result# (try ~@body (catch :default e# e#))]
         (if (nil? result#)
           (cljs.core.async/close! chan#)
           (cljs.core.async/>! chan# result#))))
     chan#))

(defmacro <? [ch]
  `(let [val# (cljs.core.async/<! ~ch)]
     (if (instance? js/Error val#)
       (throw val#)
       val#)))
