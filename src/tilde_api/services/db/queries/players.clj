(ns tilde-api.services.db.queries.players)

(defn get-all-players []
  [(str "SELECT id, social_id, image, email, name "
    "FROM players")])

(defn get-player [id]
  [(str "SELECT id, social_id, image, email, name "
    "FROM players "
    "WHERE id=?") id])

(defn update-phone! [id phone]
  [(str "UPDATE players "
    "SET phone_number=? "
    "WHERE id=?") phone id])

(defn find-player-by-email [email]
  [(str "SELECT id, social_id, image, email, name "
    "FROM players "
    "WHERE email=?") email])

(defn create-player! [{email :email image :image social_id :social_id name :name}]
  [(str "INSERT INTO players (email, image, social_id, name) "
    "VALUES (?, ?, ?, ?)") email image social_id name])

(defn update-player! [id {email :email image :image social_id :social_id name :name}]
  [(str "UPDATE players SET email=?, image=?, social_id=?, name=? "
    "WHERE id=?") email image social_id name id])
