(ns serverless.interceptors.env
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [goog.object :as gobj]))

(defn- env->hash-map [process-env]
  (->> (zipmap (gobj/getKeys process-env) (gobj/getValues process-env))
       (into {})
       (cske/transform-keys csk/->kebab-case-keyword)))

(def assoc-raw-env
  {:name ::assoc-raw-env
   :enter #(assoc % ::raw-env (j/get js/process :env))})

(def assoc-env
  {:name ::assoc-env
   :enter (fn [{:keys [::raw-env] :as ctx}]
            (assoc ctx ::env (env->hash-map raw-env)))})
