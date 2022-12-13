(ns serverless.interop
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj ->js]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [serverless.core.async :refer [go-try]]))

(defn ->clj-kebab-case
  [obj]
  (cske/transform-keys csk/->kebab-case-keyword (->clj obj :keywordize-keys false)))

(defn ->jsCamelCase
  [obj]
  (->js (cske/transform-keys csk/->camelCase obj)))

(defn promise->chan
  [promise]
  (go-try
    (->clj-kebab-case (<p! promise))))

(defn promise-fn->chan
  [promise-fn arguments]
  (-> arguments ->jsCamelCase promise-fn promise->chan))
