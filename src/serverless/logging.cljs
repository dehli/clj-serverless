(ns serverless.logging
  (:require [cljs.pprint :refer [pprint]]
            [serverless.env :refer [logging-level]]))

(defn log-debug [x]
  (when (= logging-level "DEBUG") (pprint x))
  x)
