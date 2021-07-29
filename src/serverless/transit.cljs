(ns serverless.transit
  (:require [cognitect.transit :as t])
  (:refer-clojure :exclude [read]))

(def ^:private default-reader (t/reader :json))
(def ^:private default-writer (t/writer :json))

(defn read
  ([x] (read default-reader x))
  ([reader x] (t/read reader x)))

(defn write
  ([x] (write default-writer x))
  ([writer x] (t/write writer x)))
