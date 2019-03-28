(ns serverless.core
  (:require [serverless.aws.core]
            [serverless.env]
            [serverless.json])
  (:require-macros [serverless.core :refer [import-vars]]))

(import-vars 'serverless.aws.core)
(import-vars 'serverless.env)
(import-vars 'serverless.json)
