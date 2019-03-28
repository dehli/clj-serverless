(ns serverless.aws.step-functions
  (:require [goog.object :as gobj]
            [serverless.aws.sdk :refer [AWS js-call]]
            [serverless.json :refer [clj->json]]))

(defonce ^:private StepFunctions (gobj/get AWS "StepFunctions"))
(defonce ^:private step-functions (StepFunctions.))

(defn start-execution [state-machine input]
  (js-call step-functions
           "startExecution"
           {:stateMachineArn state-machine
            :input (clj->json input)}))
