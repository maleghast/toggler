(defproject toggler "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.12.1"]
                 [compojure "1.1.8"]
                 [cheshire "5.3.1"]
                 [ring "1.3.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler toggler.handler/app}
  :main toggler.handler
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
