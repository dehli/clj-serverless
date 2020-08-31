(ns serverless.interceptors.raw-env
  (:require [applied-science.js-interop :as j]))

(def assoc-raw-env
  {:name :serverless/assoc-raw-env
   :enter #(assoc % :serverless/raw-env (j/get js/process :env))})
