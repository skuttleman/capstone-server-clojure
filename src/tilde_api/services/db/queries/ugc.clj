(ns tilde-api.services.db.queries.ugc)

(defn get-wip [id]
  [(str "SELECT * FROM game_wips "
    "WHERE id=?") id])

(defn list-my-wips [user-id]
  [(str "SELECT * FROM game_wips "
    "WHERE creator_id=?") user-id])

(defn delete-wip! [id]
  [(str "DELETE FROM game_wips "
    "WHERE id=?") id])

(defn create-wip!
  ([creator-id name initial-state] (create-wip! creator-id name initial-state nil))
  ([creator-id name initial-state forked-id]
    [(str "INSERT INTO game_wips (creator_id, name, initial_state, forked_level_id) "
      "VALUES (?, ?, ?, ?)") creator-id name initial-state forked-id]))

(defn update-wip! [id name state]
  [(str "UPDATE game_wips SET name=?, initial_state=?, updated_at=CURRENT_TIMESTAMP "
    "WHERE id=?") name state id])

(defn insert-published! [name initial-state creator-id]
  [(str "INSERT INTO game_levels (name, initial_state, creator_id) "
    "VALUES (?, ?, ?)") name initial-state creator-id])

(defn list-my-published [id]
  [(str "SELECT * FROM game_levels WHERE creator_id=?") id])
