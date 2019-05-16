(ns serverless.date)

(defn now []
  (js-invoke js/Date "now"))
