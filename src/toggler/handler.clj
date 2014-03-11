(ns toggler.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [liberator.core :refer [resource defresource]]
            [cheshire.core :refer :all]))

(defn read-default-config []
  (decode (slurp "resources/config.json") true))

(def cfg (atom (read-default-config)))

(defn getoggle
  ([] @cfg)
  ([component] (get @cfg (keyword component)))
  ([component setting] (get-in @cfg [(keyword component) (keyword setting)])))

(defn setoggle [component setting newval]
  (swap! cfg assoc-in [(keyword component) (keyword setting)] newval))

(defn reload-config [config]
  (reset! cfg config))

(defn reset-cfg []
  (reload-config (read-default-config)))

(defresource status
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok "Toggler - This is not a functional endpoint; Sorry! :-(")

(defresource get-toggle-for-component-and-setting [component setting]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e (getoggle component setting)]
                    (if-not (nil? e)
                      {::entry e})))
  :handle-ok ::entry)

(defresource get-toggle-all-for-component [component]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e (getoggle component)]
                    (if-not (nil? e)
                      {::entry e})))
  :handle-ok ::entry)

(defresource get-toggle-all
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (getoggle)))

(defresource set-toggle [component setting newval]
  :allowed-methods [:put]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e (getoggle component setting)]
                    (if-not (nil? e)
                      {::entry e})))
  :can-put-to-missing? false
  :put! (fn [_] (setoggle component setting newval))
  :handle-ok (encode newval))

(defresource reconfigure [config]
  :allowed-methods [:put]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e @cfg]
                    (if-not (nil? e)
                      {::entry e})))
  :can-put-to-missing? false
  :put! (fn [_] (reload-config config))
  :handle-ok (encode config))

(defresource reset-config
  :allowed-methods [:put]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e @cfg]
                    (if-not (nil? e)
                      {::entry e})))
  :can-put-to-missing? false
  :put! (fn [_] (reset-cfg))
  :handle-ok true)

(defroutes app-routes
  (GET "/" [] status)
  (PUT "/reconfigure" {body :body} (let [bodydecoded (decode (slurp body) true)]
                                     (reconfigure bodydecoded)))
  (PUT "/reset" [] reset-config)
  (GET "/toggle" [] get-toggle-all)
  (GET "/toggle/:component" [component] (get-toggle-all-for-component component))
  (GET "/toggle/:component/:setting" [component setting] (get-toggle-for-component-and-setting component setting))
  (PUT "/toggle" {body :body} (let [bodydecoded (decode (slurp body) true)]
                                (set-toggle (get bodydecoded :component) (get bodydecoded :setting) (get bodydecoded :newval))))
  (comment (POST "/toggle" {body :body} (create-toggle body)))
  (route/not-found "Not Found")
  (route/resources "/"))

(def app
  (handler/api app-routes))
