(ns tilde-api.services.db.queries.games)

(defn create-game! [player1-id player2-id user-id game-level-id]
  [(str "INSERT INTO games "
    "(player1_id, player2_id, creator_id, game_level_id, game_status_id) "
    "VALUES (?, ?, ?, ?, 1)") player1-id player2-id user-id game-level-id])

(defn get-intial-state [id]
  [(str "SELECT initial_state FROM game_levels WHERE id=?") id])

(defn get-current-state [id]
  [(str "SELECT games.*, game_states.state "
    "FROM games "
    "INNER JOIN game_states ON games.id=game_states.game_id "
    "WHERE games.id=? "
    "ORDER BY game_states.time_stamp DESC "
    "LIMIT 1") id])

(defn insert-state! [id state]
  [(str "INSERT INTO game_states "
    "(game_id, state) "
    "VALUES (?, ?)") id state])

(defn update-game-status! [id status-id]
  [(str "UPDATE games SET game_status_id=? "
    "WHERE id=?") status-id id])

(defn list-levels []
  [(str "SELECT gl.*, p.name creator_name, p.image creator_image "
    "FROM game_levels gl "
    "LEFT JOIN players p ON p.id=gl.creator_id")])

(defn list-invitations [user-id]
  [(str "SELECT g.*, gs.status game_status, gl.creator_id publisher_id, "
    "p1.image player1_image, p1.name player1_name, "
    "p2.image player2_image, p2.name player2_name, "
    "gc.image creator_image, gc.name creator_name, "
    "gl.name game_level_name, gl.published_at, gl.creator_id publisher_id, "
    "glc.image publisher_image, glc.name publisher_name "
    "FROM games g"
    "INNER JOIN players p1 ON p1.id=g.player1_id "
    "INNER JOIN players p2 ON p2.id=g.player2_id "
    "INNER JOIN players gc ON gc.id=g.creator_id "
    "INNER JOIN game_levels gl ON gl.id=g.game_level_id "
    "INNER JOIN players glc ON glc.id=gl.creator_id "
    "INNER JOIN game_statuses gs ON gs.id=g.game_status_id "
    "WHERE (g.player1_id=? OR g.player2_id=?) AND "
    "gs.status='not started' AND "
    "NOT g.creator_id=?") user-id user-id user-id])

(defn list-pending [user-id]
  [(str "SELECT g.*, gs.status game_status, gl.creator_id publisher_id, "
    "p1.image player1_image, p1.name player1_name, "
    "p2.image player2_image, p2.name player2_name, "
    "gc.image creator_image, gc.name creator_name, "
    "gl.name game_level_name, gl.published_at, gl.creator_id publisher_id, "
    "glc.image publisher_image, glc.name publisher_name "
    "FROM games g"
    "INNER JOIN players p1 ON p1.id=g.player1_id "
    "INNER JOIN players p2 ON p2.id=g.player2_id "
    "INNER JOIN players gc ON gc.id=g.creator_id "
    "INNER JOIN game_levels gl ON gl.id=g.game_level_id "
    "INNER JOIN players glc ON glc.id=gl.creator_id "
    "INNER JOIN game_statuses gs ON gs.id=g.game_status_id "
    "WHERE gs.status='not started' AND "
    "g.creator_id=?") user-id])

(defn list-active [user-id]
  [(str "SELECT g.*, gs.status game_status, gl.creator_id publisher_id, "
    "p1.image player1_image, p1.name player1_name, "
    "p2.image player2_image, p2.name player2_name, "
    "gc.image creator_image, gc.name creator_name, "
    "gl.name game_level_name, gl.published_at, gl.creator_id publisher_id, "
    "glc.image publisher_image, glc.name publisher_name "
    "FROM games g"
    "INNER JOIN players p1 ON p1.id=g.player1_id "
    "INNER JOIN players p2 ON p2.id=g.player2_id "
    "INNER JOIN players gc ON gc.id=g.creator_id "
    "INNER JOIN game_levels gl ON gl.id=g.game_level_id "
    "INNER JOIN players glc ON glc.id=gl.creator_id "
    "INNER JOIN game_statuses gs ON gs.id=g.game_status_id "
    "WHERE (g.player1_id=? OR g.player2_id=?) AND "
    "gs.status IN ('player1 turn', 'player2 turn')") user-id user-id])

(defn list-completed [user-id]
  [(str "SELECT g.*, gs.status game_status, gl.creator_id publisher_id, "
    "p1.image player1_image, p1.name player1_name, "
    "p2.image player2_image, p2.name player2_name, "
    "gc.image creator_image, gc.name creator_name, "
    "gl.name game_level_name, gl.published_at, gl.creator_id publisher_id, "
    "glc.image publisher_image, glc.name publisher_name "
    "FROM games g"
    "INNER JOIN players p1 ON p1.id=g.player1_id "
    "INNER JOIN players p2 ON p2.id=g.player2_id "
    "INNER JOIN players gc ON gc.id=g.creator_id "
    "INNER JOIN game_levels gl ON gl.id=g.game_level_id "
    "INNER JOIN players glc ON glc.id=gl.creator_id "
    "INNER JOIN game_statuses gs ON gs.id=g.game_status_id "
    "WHERE (g.player1_id=? OR g.player2_id=?) AND "
    "gs.status = 'completed'") user-id user-id])

(defn list-rejected [user-id]
  [(str "SELECT g.*, gs.status game_status, gl.creator_id publisher_id, "
    "p1.image player1_image, p1.name player1_name, "
    "p2.image player2_image, p2.name player2_name, "
    "gc.image creator_image, gc.name creator_name, "
    "gl.name game_level_name, gl.published_at, gl.creator_id publisher_id, "
    "glc.image publisher_image, glc.name publisher_name "
    "FROM games g"
    "INNER JOIN players p1 ON p1.id=g.player1_id "
    "INNER JOIN players p2 ON p2.id=g.player2_id "
    "INNER JOIN players gc ON gc.id=g.creator_id "
    "INNER JOIN game_levels gl ON gl.id=g.game_level_id "
    "INNER JOIN players glc ON glc.id=gl.creator_id "
    "INNER JOIN game_statuses gs ON gs.id=g.game_status_id "
    "WHERE (g.player1_id=? OR g.player2_id=?) AND "
    "g.creator_id=? AND "
    "gs.status = 'rejected'") user-id user-id user-id])

(defn accept-invitation! [id]
  [(str "UPDATE games SET game_status_id=2 "
    "WHERE id=? AND game_status_id=1") id])

(defn reject-invitation! [id]
  [(str "UPDATE games SET game_status_id=5 "
    "WHERE id=? AND game_status_id=1") id])
