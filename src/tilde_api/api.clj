(ns tilde-api.api
  (:use compojure.core)
  (:require [tilde-api.api.v2 :as v2]))

(defroutes core
  (context "/v2" [] v2/core))
