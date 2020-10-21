(ns serverless.aws.sdk
  (:require [applied-science.js-interop :as j]
            [camel-snake-kebab.core :as csk]
            [cljs-bean.core :refer [->js]]
            [serverless.interop :refer [promise->chan]]
            [serverless.utils.map :as mu]))

(def AWS
  "The AWS SDK object."
  (js/require "aws-sdk"))

(defn service
  "Constructs an AWS service interface object.

  Examples of possible usages:

  (aws/service [:api-gateway-management-api])

  (aws/service [:dynamo-d-b :document-client]
               {:params {:table-name \"table-name\"}})

  Uses camel-snake-kebab to allow for the services to be created idiomatically.

  Note: Only keywords are transformed, if you don't want to have the keyword
        transformed to PascalCase, you can provide it as a string."
  ([keys] (service keys {}))
  ([keys options]
   (let [Service (apply j/get-in [AWS (map csk/->PascalCase keys)])]
     (->> options
          (mu/kv-transform-keywords csk/->camelCase
                                    #(mu/transform-keywords csk/->PascalCase %))
          ->js
          (new Service)))))

(defn call
  "Calls to an AWS service, returning a core.async promise-chan.

  Uses camel-snake-kebab to allow you to idiomatically call the service.

  Converts SDK response back to kebab-case."
  [service method params]
  (-> service
      (j/call (csk/->camelCase method)
              (->js (mu/transform-keywords csk/->PascalCase params)))
      (j/call :promise)
      promise->chan))
