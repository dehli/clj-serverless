(ns serverless.aws.errors
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk])
  (:refer-clojure :exclude [name]))

(defn name [error]
  (csk/->kebab-case-keyword (j/get error :name)))
