(ns toggler.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [liberator.core :refer [resource defresource]]
            [cheshire.core :refer :all]
            [ring.adapter.jetty :refer (run-jetty)]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(def backup-counter (atom 0))

(def filepath (atom "./"))

(defn read-default-config []
  (decode (slurp (str @filepath "config.json")) true))

(def cfg (atom (read-default-config)))

(defn read-custom-config [filename]
  (decode (slurp (str @filepath (str filename ".json"))) true))

(defn getoggle
  ([] @cfg)
  ([component] (get @cfg (keyword component)))
  ([component setting] {:value (get-in @cfg [(keyword component) (keyword setting)])}))

(defn setoggle [component setting newval]
  (swap! cfg assoc-in [(keyword component) (keyword setting)] newval))

(defn createtoggle
  ([component-name component-toggles]
   (swap! cfg assoc component-name component-toggles))
  ([component setting newval]
  (let [toggle-map (get @cfg (keyword component))]
    (swap! cfg assoc-in [(keyword component)] (assoc toggle-map (keyword setting) newval)))))

(defn reload-config [config]
  (reset! cfg config))

(defn reset-cfg
  ([] (reload-config (read-default-config)))
  ([filename] (reload-config (read-custom-config filename))))

(defn backup-config []
  (if (.exists (clojure.java.io/as-file (str @filepath (str "backup-" @backup-counter ".json"))))
    (spit
      (str @filepath (str "backup-" @backup-counter ".json"))
      (slurp (io/file @filepath (str "backup-" (- @backup-counter 1) ".json"))))
    (spit
      (str @filepath (str "backup-" @backup-counter ".json"))
      (encode (read-default-config))))
  (swap! backup-counter inc))

(defn saveconfig
  ([]
   (backup-config)
   (spit (str @filepath "config.json") (encode @cfg)))
  ([filename]
   (spit (str @filepath (str filename ".json")) (encode @cfg))))

(defn only-files
  "Filter a sequence of files/directories by the .isFile property of
  java.io.File"
  [file-s]
  (filter #(.isFile %) file-s))

(defn file-names
  "Return the .getName property of a sequence of files"
  [file-s]
  (map #(.getName %) file-s))

(defn remove-suffix
  "return a filelist without the file suffices"
  [file-s]
  (map #(str/join "." (drop-last (str/split (str %) #"\."))) file-s))

(defn getconfigs
  ([]
   (->> (file-seq (clojure.java.io/file @filepath))
        (only-files)
        (file-names)
        (remove-suffix)
        (vec))))

(defresource status
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok "{\"message\" : \"Toggler - This is not a functional endpoint; Sorry! :-(\"}")

(defresource get-toggle-value-for-component-and-setting [component setting]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e (getoggle component setting)]
                    (if-not (nil? e)
                      {::entry e})))
  :handle-ok ::entry)

(defresource get-toggles-for-component [component]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e (getoggle component)]
                    (if-not (nil? e)
                      {::entry e})))
  :handle-ok ::entry)

(defresource get-toggles
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

(defresource create-toggle [component setting newval]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [_] (createtoggle component setting newval))
  :handle-ok (encode {:component component :setting setting :newval newval}))

(defresource create-toggles-component [component-name component-toggles]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [_] (createtoggle component-name component-toggles))
  :handle-ok (encode {:component-name component-name :component-toggles component-toggles}))

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

(defresource save-config
 :allowed-methods [:put]
  :available-media-types ["application/json"]
  :exists? (fn [_] (let [e @cfg]
                    (if-not (nil? e)
                      {::entry e})))
  :can-put-to-missing? false
  :put! (fn [_] (saveconfig))
  :handle-ok true )

(defresource save-config-to-file [filename]
 :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [_] (saveconfig filename))
  :handle-ok (encode {:filename filename}) )

(defresource load-config-from-file [filename]
 :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [_] (reset-cfg filename))
  :handle-ok (encode {:filename filename}) )

(defresource get-configs
 :allowed-methods [:get]
 :available-media-types ["application/json"]
 :handle-ok (fn [_] (encode (getconfigs))))

(defroutes app-routes
  (GET "/" [] status)
  (PUT "/reconfigure" {body :body} (let [bodydecoded (decode (slurp body) true)]
                                     (reconfigure bodydecoded)))
  (GET "/configs" [] get-configs)
  (PUT "/reset" [] reset-config)
  (POST "/reset" {body :body} (let [bodydecoded (decode (slurp body) true)]
                               (load-config-from-file (get bodydecoded :filename))))
  (PUT "/save" [] save-config)
  (POST "/save" {body :body} (let [bodydecoded (decode (slurp body) true)]
                               (save-config-to-file (get bodydecoded :filename))))
  (GET "/toggle" [] get-toggles)
  (GET "/toggle/:component" [component] (get-toggles-for-component component))
  (GET "/toggle/:component/:setting" [component setting] (get-toggle-value-for-component-and-setting component setting))
  (PUT "/toggle" {body :body} (let [bodydecoded (decode (slurp body) true)]
                                (set-toggle (get bodydecoded :component) (get bodydecoded :setting) (get bodydecoded :newval))))
  (POST "/toggle" {body :body} (let [bodydecoded (decode (slurp body) true)]
                                 (if (= (count bodydecoded) 3)
                                   (create-toggle (get bodydecoded :component) (get bodydecoded :setting) (get bodydecoded :newval))
                                   (create-toggles-component (keyword (get bodydecoded :component-name)) (get bodydecoded :component-toggles)))))
  (route/not-found "Not Found")
  (route/resources "/"))

(def app
  (handler/api app-routes))

(defn -main [& args]
  (if (and
       args
       (= (count args) 1))
      (reset! filepath (first args)))
  (run-jetty app {:port 7000 :join? false}))
