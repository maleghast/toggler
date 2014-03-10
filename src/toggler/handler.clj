(ns toggler.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [liberator.core :refer [resource defresource]]
            [cheshire.core :refer :all]))

(def cfg (atom (decode (slurp "resources/config.json") true)))

(defn getoggleall [component]
  (get @cfg (keyword component)))

(defn getoggle [component setting]
  (get-in @cfg [(keyword component) (keyword setting)]))

(defresource status
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok "Toggler - This is not a functional endpoint; Sorry! :-(")

(defresource get-toggle [component setting]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (getoggle component setting)))

(defresource get-toggle-all [component]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (getoggleall component)))

(comment
  (defresource set-toggle [component setting]
  :allowed-mothods [:put]
  :available-media-types ["application/json"]))

(defroutes app-routes
  (GET "/" [] status)
  (GET "/toggle/:component" [component] (get-toggle-all component))
  (GET "/toggle/:component/:setting" [component setting] (get-toggle component setting))
  (comment (PUT "/toggle/:component/:setting" [component setting value] (set-toggle component setting value)))
  (comment (POST "/toggle/new" {params :form-params} (create-toggle params)))
  (route/not-found "Not Found")
  (route/resources "/"))

(def app
  (handler/site app-routes))
