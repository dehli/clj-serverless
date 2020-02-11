(ns serverless.logger
  (:require [clojure.string :refer [lower-case]]))

(def ^:private level->number
  {"debug" 0
   "info" 1
   "warn" 2
   "error" 3
   "off" 4
   "" 5})

(defn- make-logger
  [{:keys [env-level required-level logger]}]
  (fn [x]
    (when (>= (level->number required-level)
              (level->number (lower-case (or env-level ""))))
      (logger x))))

(defn context->deps
  ([context] (context->deps prn context))
  ([logger {{:keys [logging-level]} :env}]
   (let [make-logger #(make-logger {:env-level logging-level
                                    :required-level %
                                    :logger logger})]
     #:logger
     {:log-debug (make-logger "debug")
      :log-info (make-logger "info")
      :log-warn (make-logger "warn")
      :log-error (make-logger "error")})))
