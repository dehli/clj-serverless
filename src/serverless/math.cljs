(ns serverless.math)

(def ^:private radians-per-degree
  (/ js/Math.PI 180))

(defn degrees->radians [degrees]
  (* degrees radians-per-degree))

(defn radians->degrees [radians]
  (/ radians radians-per-degree))
