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
   :enter (fn [{:keys [serverless/raw-env] :as context}]
            (assoc context :serverless/env (env->hash-map raw-env)))})
