(ns serverless.interceptors.core
  (:require [serverless.core.async :refer [go-try channel? <!]]))

(defn- doseq-interceptors!
  [fn-key context interceptors]
  (go-try
    (doseq [interceptor interceptors]
      (let [next-fn (or (get interceptor fn-key) identity)
            chan-or-val (next-fn @context)
            new-context (if (channel? chan-or-val)
                          (<! chan-or-val)
                          chan-or-val)]
        (reset! context new-context)))))

(defn add-interceptors
  [interceptors handler]
  (fn [event]
    (go-try
      (let [context (atom event)]
        ;; Go through interceptors (on enter)
        (<! (doseq-interceptors!
             :enter context
             (concat interceptors [{:name :handler :enter handler}])))

        ;; Go through interceptors (on leave)
        (<! (doseq-interceptors!
             :leave context
             (reverse interceptors)))

        ;; Return context
        @context))))
