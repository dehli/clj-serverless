(ns serverless.transit
  (:require [cognitect.transit :as t])
  (:refer-clojure :exclude [read]))

(def ^:private default-reader (t/reader :json))
(def ^:private default-writer (t/writer :json))

(defn read [x]
  (t/read default-reader x))

(defn write [x]
  (t/write default-writer x))
