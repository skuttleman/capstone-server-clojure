(ns tilde-api.services.logger
  (:require [clojure.string :as s]))

(defn ^:private caps [kw]
  (->> kw
    (str)
    (s/upper-case)
    (rest)
    (apply str)))

(defn in [app]
  (fn [request]
    (-> request
      (:request-method)
      (caps)
      (str " \"" (request :uri) "\"")
      (println))
    (app request)))

(defn user [app]
  (fn [request]
    (->> request
      (:user)
      (str "user: ")
      (println))
    (app request)))
