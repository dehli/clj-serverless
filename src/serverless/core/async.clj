(ns serverless.core.async)

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

(defmacro <<? [chans]
  `(let [res# (atom [])]
     (doseq [c# ~chans]
       (swap! res# conj (serverless.core.async/<? c#)))
     @res#))
