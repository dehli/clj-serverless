(ns serverless.core.async
  (:require [cljs.core.async :as async])
  (:require-macros [serverless.core.async]))

(defn channel? [c]
  (instance? cljs.core.async.impl.channels.ManyToManyChannel c))

(def <! async/<!)
