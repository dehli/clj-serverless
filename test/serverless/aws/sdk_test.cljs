(ns serverless.aws.sdk-test
  (:require [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.aws.sdk :as sut]))

(deftest format-options
  (is (= (#'sut/format-options
          {:params {:table-name "my-table-name"}
           :convert-empty-values true})

         {:params {:TableName "my-table-name"}
          :convertEmptyValues true})))

(deftest aws-call
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
