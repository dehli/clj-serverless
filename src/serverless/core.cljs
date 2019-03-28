(ns serverless.core
  (:require [serverless.aws.api-gateway]
            [serverless.aws.core]
            [serverless.aws.dynamo-db]
            [serverless.aws.step-functions]
            [serverless.env]
            [serverless.json])
  (:require-macros [serverless.core :refer [import-vars]]))

(import-vars 'serverless.aws.api-gateway)
(import-vars 'serverless.aws.core)
(import-vars 'serverless.aws.dynamo-db)
(import-vars 'serverless.aws.step-functions)

(import-vars 'serverless.env)
(import-vars 'serverless.json)
