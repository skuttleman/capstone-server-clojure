(ns tilde-api.app
  (:use compojure.core
        org.httpkit.server)
  (:require [compojure.handler :refer [site]]
            [compojure.route :refer [not-found]]
            [environ.core :refer [env]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.cors :refer [wrap-cors]]
            [tilde-api.auth :as auth-api]
            [tilde-api.services.auth :as auth]
            [tilde-api.services.request-parser :as parser]
            [tilde-api.services.logger :as logger]
            [tilde-api.services.socket :refer [socket send-message!]]
            [tilde-api.api :as api]))

(defroutes app-routes
  (GET "/healthcheck" [] {:status 200 :body {:health "OK"}})
  (GET "/socket" request (socket request))
  (POST "/send-message" { {email :email key :key payload :payload} :body} (send-message! email key payload) {:body {:message :ok}})
  (context "/auth" [] auth-api/core)
  (context "/api" [] api/core)
  (not-found {:status 404 :body {:message "Unknown resource"}}))

(def app
  (-> app-routes
    (wrap-json-response)
    (auth/block)
    (parser/parse-query)
    (parser/parse-body)
    (logger/user)
    (auth/authenticate)
    (logger/in)
    (wrap-cors #".*")))

(defn -main [& args]
  (let [port (or (env :port) 8000)]
    (-> :env
      (env)
      (= "development")
      (if
        (wrap-reload (site #'app))
        (site app))
      (run-server {:port port}))
    (println (str "Server is listening on port: " port))))
