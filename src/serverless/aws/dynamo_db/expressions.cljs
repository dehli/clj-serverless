(ns aws.dynamo-db.expressions
  (:require [clojure.string :refer [join replace]])
  (:refer-clojure :exclude [replace]))

(defn- escape [v]
  (-> v str (replace #"-" "") (replace #":" "")))

(defn ->attribute-name-key [v] (str "#" (escape v)))
(defn ->attribute-value-key [v] (str ":" (escape v)))

(defn ->set-expression [props]
  (->> (keys props)
       (map #(str (->attribute-name-key %) " = " (->attribute-value-key %)))
       (join ", ")
       (str "set ")))

(defn ->attribute-names [props]
  (->> (keys props)
       (map #(vector (->attribute-name-key %) %))
       (into {})))

(defn ->attribute-values [props]
  (->> (keys props)
       (map #(vector (->attribute-value-key %) (get props %)))
       (into {})))
