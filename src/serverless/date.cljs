(ns serverless.date)

(defn now
  "Seconds since epoch (now)"
  []
  (js-invoke js/Date "now"))

(defn ->iso-str
  "Converts datetime object into ISO string"
  [datetime-obj]
  (js-invoke (new js/Date datetime-obj) "toISOString"))

(defn now-iso-str
  "Now ISO string"
  []
  (->iso-str (now)))

(defn ->timestamp
  "Converts any format-table js datetime object to seconds from epoch"
  [datetime-object]
  (js-invoke (new js/Date datetime-object) "getTime"))

(defn before?
  "t1 is before t2"
  [t1 t2]
  (< (->timestamp t1) (->timestamp t2)))

(defn after?
  "t1 is after t2"
  [t1 t2]
  (> (->timestamp t1) (->timestamp t2)))

(defn equal?
  "t1 is equal to t2"
  [t1 t2]
  (= (->timestamp t1) (->timestamp t2)))
