(ns serverless.env
  (:require [clojure.string :as string]
            [goog.object :as gobj]))

(defn env
  ([] (gobj/get js/process "env"))
  ([key] (gobj/get (env) key)))

(defn- env-var->keyword [env-var]
  "Ex: AWS_REGION -> :aws-region"
  (-> env-var string/lower-case (string/replace "_" "-") keyword))

(defn env->hash-map
  ([] (env->hash-map (env)))
  ([env] (->> (zipmap (gobj/getKeys env) (gobj/getValues env))
              (map (fn [[k v]] [(env-var->keyword k) v]))
              (into {}))))


(def aws-lambda-function-name
  (env "AWS_LAMBDA_FUNCTION_NAME"))

(def aws-region
  (env "AWS_REGION"))

(def logging-level
  (env "LOGGING_LEVEL"))
