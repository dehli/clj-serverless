(ns serverless.case-styles
  (:require [clojure.set :refer [rename-keys]]
            [clojure.string :refer [capitalize join split]]))

(defn- kebab-str->pascal-str [string]
  (->> (split string "-") (map capitalize) join))

(defn- kebab-key->pascal-key [key]
  (cond-> (kebab-str->pascal-str (name key))
    (keyword? key) keyword))

(defn kebab->pascal [obj]
  (->> (keys obj)
       (map #(vector % (kebab-key->pascal-key %)))
       (into {})
       (rename-keys obj)))
