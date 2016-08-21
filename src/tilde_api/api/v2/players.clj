(ns tilde-api.api.v2.players
  (:use compojure.core)
  (:require [tilde-api.services.socket :refer [filter-online]]
            [tilde-api.services.db.queries :refer [list-players]]))

(defroutes core
  (GET "/" {{status :status} :query}
    {:body {:users
      (let [players (list-players)]
        (cond
          (= status "offline") (filter filter-online players)
          :else players))}}))
