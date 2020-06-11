(ns serverless.aws.errors
  (:require [goog.object :as gobj]))

(defn- property-matches? [property value error]
  (= (gobj/get error property) (name value)))

(defn message-matches? [value error]
  (property-matches? "message" value error))
(defn name-matches? [value error]
  (property-matches? "name" value error))

(defn conditional-check-failed-exception? [error]
  (name-matches? :ConditionalCheckFailedException error))
(defn invalid-parameter-exception? [error]
  (name-matches? :InvalidParameterException error))
(defn not-authorized-exception? [error]
  (name-matches? :NotAuthorizedException error))
(defn user-not-found-exception? [error]
  (name-matches? :UserNotFoundException error))
(defn validation-exception? [error]
  (name-matches? :ValidationException error))
