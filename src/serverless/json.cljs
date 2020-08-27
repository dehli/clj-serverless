(ns serverless.json
  (:require [cljs-bean.core :refer [->clj ->js]]))

(defn clj->json [clj-obj]
  (-> clj-obj ->js js/JSON.stringify))

(defn json->clj [json-str]
  (-> json-str js/JSON.parse ->clj))
