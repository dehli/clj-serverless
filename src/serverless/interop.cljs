(ns serverless.interop
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]
            [cljs-bean.core :refer [->clj]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [serverless.core.async :refer [go-try]]))

(defn ->clj-kebab-case
  [obj]
  (cske/transform-keys csk/->kebab-case (->clj obj)))

(defn promise->chan
  [promise]
  (go-try
    (->clj-kebab-case (<p! promise))))
