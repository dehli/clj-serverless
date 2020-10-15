(ns serverless.aws.errors
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]))

(defn code
  [error]
  (-> error (j/get :code) csk/->kebab-case-keyword))

(defn message
  [error]
  (j/get error :message))
