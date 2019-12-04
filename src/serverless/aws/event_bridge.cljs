(ns serverless.aws.event-bridge
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]))

(defonce ^:private EventBridge (gobj/get AWS "EventBridge"))

(defn event-bridge []
  (new EventBridge))

(defn put-events
  [client entries]
  (js-call client "putEvents" {:Entries entries}))
