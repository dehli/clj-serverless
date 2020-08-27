(ns serverless.interceptors.env
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [goog.object :as gobj]))

(defn- env->hash-map [env]
  (->> (zipmap (gobj/getKeys env) (gobj/getValues env))
       (into {})
       (cske/transform-keys csk/->kebab-case-keyword)))

(def assoc-env
  {:name :serverless/assoc-env
   :enter #(assoc-in % [:request :serverless/env]
                     (env->hash-map (j/get js/process :env)))})
