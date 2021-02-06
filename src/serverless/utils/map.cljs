(ns serverless.utils.map
  (:require [clojure.walk :refer [postwalk]]))

(defn kv-transform-keywords
  "Transform a map's keywords and optionally their values as well.

  Takes key transform function, value transform function, and a map.
  If a non-map is passed in, the value is returned."
  ([kt coll] (kv-transform-keywords kt identity coll))
  ([kt vt coll]
   (if (map? coll)
     (->> coll
          (map (fn [[k v]] [(if (keyword? k) (kt k) k) (vt v)]))
          (into {}))
     coll)))

(defn transform-keywords
  "Recursively transforms all keyword map keys in coll with t."
  [t coll]
  (postwalk #(kv-transform-keywords t %) coll))

(defn map->nsmap
  "Adds ns to each unqualified keyword in map

  Example usage:

  (map->nsmap {:x 0 :foo/y 1} \"abc\") ;; -> {:abc/x 0 :foo/y 1}
  "
  [map ns]
  (reduce-kv (fn [acc k v]
               (let [new-kw (if (and (keyword? k)
                                     (not (qualified-keyword? k)))
                              (keyword (str ns) (name k))
                              k)]
                 (assoc acc new-kw v)))
             {}
             map))
