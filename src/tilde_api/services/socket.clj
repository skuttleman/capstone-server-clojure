(ns tilde-api.services.socket
  (:use org.httpkit.server)
  (:require [tilde-api.services.auth :refer [token->user unauthorized]]
            [clojure.data.json :as json]))

(def ^:private user-list (atom []))

(defn ^:private remove-user! [list channel]
  (filter (fn [item] (not= (:channel item) channel)) list))

(defn ^:private add-user! [list user channel]
  (conj list {:user user :channel channel}))

(defn ^:private send-json! [channel payload]
  (->> payload
    (json/write-str)
    (send! channel)))

(defn ^:private broadcast! [payload]
  (println (str "broadcast to " (count (deref user-list)) " players."))
  (doseq [{channel :channel} (deref user-list)]
    (send-json! channel payload)))

(defn ^:private broadcast-user-list! []
  (->> user-list
    (deref)
    (map :user)
    (distinct)
    (assoc {} "user list")
    (broadcast!)))

(defn ^:private request->user [{query :query}]
  (->> query
    (:token)
    (token->user)
    (:user)))

(defn socket [request]
  (let [user (request->user request)]
    (if user
      (with-channel request channel
        (swap! user-list add-user! user channel)
        (broadcast-user-list!)
        (on-close channel (fn [status]
          (swap! user-list remove-user! channel)
          (broadcast-user-list!))))
      (unauthorized))))

(defn send-message! [email key payload]
  (->> user-list
    (deref)
    (filter (fn [{ {socket-email :email} :user}]
      (= email socket-email)))
    ((fn [list]
      (doseq [{channel :channel} list]
        (send-json! channel {key payload}))))))

(defn filter-online [{player-id :id}]
  (->> user-list
    (deref)
    (map (comp :id :user))
    (filter (partial = player-id))
    (count)
    (= 0)))
