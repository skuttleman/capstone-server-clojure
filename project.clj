(defproject tilde-api "0.1.0-SNAPSHOT"
  :description "API v2 for legendoftilde.com"
  :main tilde-api.app
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "0.5.0"]
                 [compojure "1.5.1"]
                 [sudharsh/clj-oauth2 "0.5.3"]
                 [clj-jwt "0.1.1"]
                 [ring/ring-devel "1.3.2"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.2.1"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [clj-http "2.2.0"]
                 [clj-time "0.12.0"]
               	 [mysql/mysql-connector-java "5.1.39"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [http-kit "2.1.18"]
                 [twilio-api "1.0.1"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
                 [org.clojure/data.json "0.2.6"]])
