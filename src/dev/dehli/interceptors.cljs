(ns dev.dehli.interceptors
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [goog.object :as gobj]
            [potpuri.core :as pot]))

(def merge-system-env
  {:name ::merge-system-env
   :enter (fn [ctx]
            (let [env (j/get js/process :env)]
              (->> (zipmap (gobj/getKeys env) (gobj/getValues env))
                   (into {})
                   (pot/map-keys #(keyword "system.env" (csk/->kebab-case-string %)))
                   (merge ctx))))})
