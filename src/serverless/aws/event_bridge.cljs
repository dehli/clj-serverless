(ns serverless.aws.event-bridge
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]
            [serverless.json :refer [clj->json]]))

(defonce ^:private EventBridge (gobj/get AWS "EventBridge"))

(defn event-bridge []
  (new EventBridge))

(defn put-events
  ([client entries] (put-events client {} entries))
  ([client defaults entries]
   (let [update-entry #(-> defaults (merge %) (update :Detail clj->json))]
     (js-call client "putEvents" {:Entries (map update-entry entries)}))))
