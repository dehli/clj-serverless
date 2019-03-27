(ns serverless.json)

(defn clj->json [x]
  (-> x clj->js js/JSON.stringify))

(defn json->clj [x & opts]
  (apply js->clj (conj opts (js/JSON.parse x))))
