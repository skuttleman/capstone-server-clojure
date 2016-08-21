(ns tilde-api.services.db.queries
  (:require [tilde-api.services.db.actions.player :as pa]))

(defn create-or-update-db! [user]
  (->> user
    (:email)
    (pa/find-by-email)
    ((fn [{id :id}]
      (if id
        (pa/update! id user)
        (pa/create! user))))))

(def list-players pa/list-players)
