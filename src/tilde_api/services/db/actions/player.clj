(ns tilde-api.services.db.actions.player
  (:require [tilde-api.services.db.connection :refer [execute! query]]
            [tilde-api.services.db.queries.players :as pq]))

(defn find-by-email [email]
  (-> email
    (pq/find-player-by-email)
    (query)
    (first)))

(defn create! [user]
  (->> user
    (pq/create-player!)
    (execute! "players")
    (assoc user :id)))

(defn update! [id user]
  (->> user
    (pq/update-player! id)
    (execute! "players"))
  (assoc user :id id))

(defn list-players []
  (->>
    (pq/get-all-players)
    (query)))
