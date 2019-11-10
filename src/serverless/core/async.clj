(ns serverless.core.async
  (:require [cljs.core.async :as async]))

(defmacro go-try
  [& body]
  `(let [chan# (async/promise-chan)]
     (async/go
       (let [result# (try ~@body (catch :default e# e#))]
         (if (nil? result#)
           (async/close! chan#)
           (async/>! chan# result#))))
     chan#))

(defmacro <? [ch]
  `(let [val# (async/<! ~ch)]
     (if (instance? js/Error val#)
       (throw val#)
       val#)))

(defmacro <<? [chans]
  `(let [res# (atom [])]
     (doseq [c# ~chans]
       (swap! res# conj (serverless.core.async/<? c#)))
     @res#))
