(ns serverless.interceptors.env
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [goog.object :as gobj]))

(defn- env->hash-map [env]
  (->> (zipmap (gobj/getKeys env) (gobj/getValues env))
       (into {})
       (cske/transform-keys csk/->kebab-case-keyword)))

(def assoc-env
  {:name :serverless/assoc-env
   :enter (fn [context]
            (assoc-in context
                      [:request :serverless/env]
                      (-> context
                          (get-in [:request :serverless/raw-env])
                          env->hash-map)))})
