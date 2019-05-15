(ns serverless.json)

(defn keyword->str
  [k]
  (-> k str (subs 1)))

(defn clj->json
  [& x]
  (-> clj->js (apply x) js/JSON.stringify))

(defn json->clj
  [x & opts]
  (apply js->clj (conj opts (js/JSON.parse x))))
