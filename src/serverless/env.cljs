(ns serverless.env
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [goog.object :as gobj]))

(defn env
  ([] (j/get js/process :env))
  ([key] (j/get (env) key)))

(defn env->hash-map
  ([] (env->hash-map (env)))
  ([env] (->> (zipmap (gobj/getKeys env) (gobj/getValues env))
              (into {})
              (cske/transform-keys csk/->kebab-case-keyword))))

(def aws-lambda-function-name
  (env :AWS_LAMBDA_FUNCTION_NAME))

(def aws-region
  (env :AWS_REGION))

(def logging-level
  (env :LOGGING_LEVEL))
