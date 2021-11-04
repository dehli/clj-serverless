(ns serverless.core.async
  (:require [cljs.core.async]
            [cljs.core.async.impl.channels])
  (:require-macros [serverless.core.async]))

(defn channel? [c]
  (instance? cljs.core.async.impl.channels/ManyToManyChannel c))
