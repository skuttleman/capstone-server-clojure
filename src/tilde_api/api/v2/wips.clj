(ns tilde-api.api.v2.wips
  (:use compojure.core)
  (:require [tilde-api.services.read-file :refer [file->json]]))

(defroutes core
  (GET "/test" [] {:body {:json_api "data" :more_data {:more "data" :and ["other" "things"]}}})
  (GET "/123" [] (file->json "src/tilde_api/api/v2/resources/123.json"))
  (GET "/:id" {user :user {id :id} :params} {:body {:user user :id id} }))
