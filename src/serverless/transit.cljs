(ns serverless.transit
  (:require [cognitect.transit :as t]
            [time-literals.read-write]
            [java.time :refer [Period
                               LocalDate
                               LocalDateTime
                               ZonedDateTime
                               Instant
                               ZoneId
                               DayOfWeek
                               LocalTime
                               Month
                               Duration
                               Year
                               YearMonth]])
  (:refer-clojure :exclude [read]))

(def time-classes
  {'period Period
   'date LocalDate
   'date-time LocalDateTime
   'zoned-date-time ZonedDateTime
   'instant Instant
   'time LocalTime
   'duration Duration
   'year Year
   'year-month YearMonth
   'zone ZoneId
   'day-of-week DayOfWeek
   'month Month})

(def write-handlers
  (into {}
    (for [[tick-class host-class] time-classes]
      [host-class (t/write-handler (constantly (name tick-class)) str)])))

(def read-handlers
  (into {} (for [[sym fun] time-literals.read-write/tags]
             [(name sym) (t/read-handler fun)])))

(def ^:private default-reader (t/reader :json {:handlers read-handlers}))
(def ^:private default-writer (t/writer :json {:handlers write-handlers}))

(defn read
  ([x] (read default-reader x))
  ([reader x] (t/read reader x)))

(defn write
  ([x] (write default-writer x))
  ([writer x] (t/write writer x)))
