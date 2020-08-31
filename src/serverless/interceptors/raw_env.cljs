(ns serverless.interceptors.raw-env
  (:require [applied-science.js-interop :as j]))

(def assoc-raw-env
  {:name :serverless/assoc-raw-env
   :enter #(assoc-in % [:request :serverless/raw-env] (j/get js/process :env))})
