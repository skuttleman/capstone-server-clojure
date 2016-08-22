(ns tilde-api.services.twilio
  (:require [tilde-api.services.db.connection :refer [query]]
            [tilde-api.services.db.queries.players :as pq]
            [twilio.core :as twilio]
            [environ.core :refer [env]]))

(defn get-phone [id]
  (->> id
    (pq/get-phone)
    (query)
    (first)
    (:phone_number)))

(defn send-sms! [id message]
  (let [phone (get-phone id)]
    (if phone
      (->>
        (twilio/with-auth (env :twilio-account-sid) (env :twilio-auth-token)
          (twilio/send-sms
            { :From (env :twilio-phone-num)
              :To (str "+1" phone)
              :Body message}))
        (deref)
        (println)))))
