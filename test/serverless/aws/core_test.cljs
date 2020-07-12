(ns serverless.aws.core-test
  (:require [cljs.core.async :refer [go <!]]
            [cljs.test :refer [async deftest is]]
            [serverless.aws.core :as sut]
            [serverless.json :refer [clj->json]]))

(deftest aws-call
  (async done
    (go
      (let [mock-service
            #js {:getItem (fn [params]
                            (is (= (js/JSON.stringify params)
                                   (clj->json {:Key {:id "i"}})))
                            #js {:promise #(js/Promise.resolve
                                            #js {:Result params})})}

            response (<! (sut/call mock-service :get-item {:Key {:id "i"}}))]

        (is (= response {:Result {:Key {:id "i"}}})))

      (done))))
