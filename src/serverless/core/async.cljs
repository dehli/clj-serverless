(ns serverless.core.async
  (:require [cljs.core.async])
  (:require-macros [serverless.core.async]))

(defn channel? [c]
  (instance? cljs.core.async.impl.channels.ManyToManyChannel c))
