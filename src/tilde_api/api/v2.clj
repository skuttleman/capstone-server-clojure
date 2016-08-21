(ns tilde-api.api.v2
  (:use compojure.core)
  (:require [tilde-api.api.v2.wips :as wips]
            [tilde-api.api.v2.players :as players]
            [tilde-api.api.v2.behaviors :as behaviors]))

(defroutes core
  (context "/wips" [] wips/core)
  (context "/players" [] players/core)
  (context "/behaviors" [] behaviors/core))
