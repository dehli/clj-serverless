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
  {'time/period Period
   'time/date LocalDate
   'time/date-time LocalDateTime
   'time/zoned-date-time ZonedDateTime
   'time/instant Instant
   'time/time LocalTime
   'time/duration Duration
   'time/year Year
   'time/year-month YearMonth
   'time/zone ZoneId
   'time/day-of-week DayOfWeek
   'time/month Month})

(def write-handlers
  (into {}
    (for [[tick-class host-class] time-classes]
      [host-class (t/write-handler (constantly tick-class) str)])))

(def read-handlers
  (into {} (for [[sym fun] time-literals.read-write/tags]
             [sym (t/read-handler fun)])))

(def ^:private default-reader (t/reader :json {:handlers read-handlers}))
(def ^:private default-writer (t/writer :json {:handlers write-handlers}))

(defn read
  ([x] (read default-reader x))
  ([reader x] (t/read reader x)))

(defn write
  ([x] (write default-writer x))
  ([writer x] (t/write writer x)))
