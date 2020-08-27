(ns serverless.aws.sdk-test
  (:require [cljs-bean.core :refer [->clj]]
            [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.aws.sdk :as sut]))

(deftest ->js-options
  (is (= (-> {:params {:table-name "my-table-name"}
              :convert-empty-values true}
             (#'sut/->js-options)
             ->clj)

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

            response (<! (sut/call mock-service
                                   :get-item {:key {:id "i"}}))]

        (is (= response {:hello-world true})))

      (done))))
