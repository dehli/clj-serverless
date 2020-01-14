(ns serverless.core
  (:require [cljs.analyzer.api :as api]))

(defmacro import-vars [[_ ns]]
  `(do
     ~@(map (fn [[k# _]]
              `(defonce ~(symbol k#)
                 ~(symbol (name ns) (name k#))))

            (api/ns-publics ns))))

(defmacro def-
  "same as def, yielding non-public def"
  ([name] `(def- ~name nil))
  ([name expr]
   (list `def (with-meta name (assoc (meta name) :private true)) expr)))
