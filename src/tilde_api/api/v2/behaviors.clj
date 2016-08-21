(ns tilde-api.api.v2.behaviors
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [tilde-api.services.read-file :refer [file->json]]))

(defroutes core
  (GET "/" [] (file->json "src/tilde_api/api/v2/resources/behaviors.json")))
