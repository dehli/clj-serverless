(ns serverless.aws.sdk-test
  (:require [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.aws.sdk :as sut]))

(deftest transform-service-options
  (is (= (#'sut/transform-service-options
          {:params {:table-name "my-table-name"}
           :convert-empty-values true})

         {:params {:TableName "my-table-name"}
          :convertEmptyValues true})))

(deftest aws-call
  (async done
    (go
      (let [mock-service
            #js {:getItem (fn [params]
                            (is (= (js->clj params) {"Key" {"id" "i"}}))
                            #js {:promise
                                 #(js/Promise.resolve #js {:HelloWorld true})})}

            response (<! (sut/call mock-service {:method :get-item
                                                 :params {:key {:id "i"}}}))]
        (is (= response {:hello-world true})))

      (done))))
