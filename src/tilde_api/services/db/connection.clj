(ns tilde-api.services.db.connection
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as jdbc]
            [environ.core :refer [env]]
            [jdbc.pool.c3p0 :as pool]))

(def connection
  (pool/make-datasource-spec
    {:classname "com.mysql.jdbc.Driver"
    :subprotocol "mysql"
    :user (env :database-user)
    :password (env :database-password)
    :subname (env :database-subname)
    :initial-pool-size 3}))

(defn query [sql-params]
  (try (jdbc/query connection sql-params)
    (catch Exception e nil)))

(defn execute! [table-name sql-params]
  (try
    (jdbc/execute! connection sql-params)
    (as-> sql-params data
      (first data)
      (re-matches #"^INSERT INTO.*" data)
      (if data
        (->> table-name
          (str "SELECT max(id) id FROM ")
          (conj [])
          (query)
          (first)
          (:id))
        :success))
      (catch Exception e :failure)))
