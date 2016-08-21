(ns tilde-api.services.auth
  (:require [clj-jwt.core :refer :all]
            [clj-time.core :refer [now plus days]]
            [clojure.string :as s]
            [environ.core :refer [env]]
            [clojure.data.json :as json]))

(def headers {"Content-Type" "application/json"})

(defn unauthorized []
  (let [response {:status 403 :headers headers}]
    (->> {:message "Unauthorized Request"}
      (json/write-str)
      (assoc response :body))))

(defn ^:private verify-jwt [token]
  (if (> (count token) 0)
    (-> token
      (str->jwt)
      (verify (env :token-secret)))
    false))

(def decode str->jwt)

(defn encode [payload]
  (-> payload
    (jwt)
    (sign :HS256 (env :token-secret))
    (to-str)))

(defn ^:private get-jwt [request]
  (->> "authorization"
    ((request :headers))
    ((fn [token] (or token "")))
    (split-at 7)
    (last)
    (apply str)))

(defn encode-user [user]
  (->> (plus (now) (days 90))
    (assoc {:user user :iat (now)} :exp)
    (encode)))

(defn token->user [jwt]
  (if (verify-jwt jwt)
    (:claims (decode jwt))))

(defn authenticate [app]
  (fn [request]
    (->> request
      (get-jwt)
      (token->user)
      (assoc request :user)
      (app))))

(defn block [app]
  (fn [request]
    (-> request
      (:uri)
      (.startsWith "/api/")
      (and (nil? (:user request)))
      (if
        (unauthorized)
        (app request)))))
