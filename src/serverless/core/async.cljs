(ns serverless.core.async
  (:require [cljs.core.async :as async])
  (:require-macros [serverless.core.async]))

(def <! async/<!)
