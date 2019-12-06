(ns serverless.aws.event-bridge
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]))

(defonce ^:private EventBridge (gobj/get AWS "EventBridge"))

(defn event-bridge []
  (new EventBridge))

(defn put-events
  ([client entries] (put-events client {} entries))
  ([client defaults entries]
   (js-call client "putEvents" {:Entries (map #(merge defaults %) entries)})))
