(defproject toggler "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [liberator "0.11.0"]
                 [compojure "1.1.6"]
                 [cheshire "5.3.1"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.1.0"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler toggler.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
