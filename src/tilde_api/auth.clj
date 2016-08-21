(ns tilde-api.auth
  (:use compojure.core)
  (:use ring.util.response)
  (:require [environ.core :refer [env]]
            [clojure.string :as s]
            [clj-oauth2.client :as oauth2]
            [tilde-api.services.auth :as auth]
            [tilde-api.services.db.queries :as queries]
            [clj-http.client :as http]))

(def oauth-config {:redirect-uri (str (env :host) "/auth/gplus/callback")
                   :client-id (env :google-client-id)
                   :client-secret (env :google-client-secret)
                   :scope ["https://www.googleapis.com/auth/userinfo.email" "https://www.googleapis.com/auth/userinfo.profile"]
                   :authorization-uri "https://accounts.google.com/o/oauth2/auth"
                   :access-token-uri "https://accounts.google.com/o/oauth2/token"
                   :access-query-param :access_token
                   :grant-type "authorization_code"
                   :access-type "online"
                   :approval_prompt ""})

(defn pop-q [{url :url}]
  (-> url
    (s/split #"\?")
    (first)))

(def redirect-url (oauth2/make-auth-request oauth-config))

(defn parse-info [[{social_id :user_id email :email} {name :displayName image :image}]]
  {:social_id social_id :email email :name name :image (pop-q image)})

(defn get-info [token]
  (->> ["/oauth2/v1/tokeninfo" "/plus/v1/people/me"]
    (map (fn [url]
      (as-> url data
        (str "https://www.googleapis.com" data)
        (http/get data {:query-params {:access_token (:access-token token)} :as :json})
        (:body data))))))

(defn send-token [token]
  (redirect (str (env :client-host) "/#/login?token=" token)))

(defn callback [request] (println (str "query: " (:query request)))
  (->> redirect-url
    (oauth2/get-access-token oauth-config (:query request))
    (get-info)
    (parse-info)
    (queries/create-or-update-db!)
    (auth/encode-user)
    (send-token)))

(defroutes core
  (POST "/gplus" [] (redirect (:uri redirect-url)))
  (GET "/gplus" [] (redirect (:uri redirect-url)))
  (GET "/gplus/callback" request (callback request)))
