(ns serverless.aws.sdk-test
  (:require [applied-science.js-interop :as j]
            [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.aws.sdk :as sut]))

(deftest service
  (let [table-name (str (random-uuid))
        ddb (sut/service [:dynamo-d-b :document-client]
                         {:params {:table-name table-name}
                          :convert-empty-values true})]

    (is (j/get-in ddb [:options :convertEmptyValues]))
    (is (= (j/get-in ddb [:options :params :TableName]) table-name))))

(deftest call
  (async done
    (go
      (let [mock-service
            #js {:getItem (fn [params]
                            (is (= (js->clj params) {"Key" {"Id" "i"}}))
                            #js {:promise
                                 #(js/Promise.resolve #js {:HelloWorld true})})}

            response (<! (sut/call mock-service
                                   :get-item {:key {:id "i"}}))]

        (is (= response {:hello-world true})))

      (done))))
