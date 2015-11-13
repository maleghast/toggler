(defproject toggler "1.0.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.12.2"]
                 [compojure "1.3.1"]
                 [cheshire "5.3.1"]
                 [ring "1.3.2"]]
  :plugins [[lein-ring "0.9.1"]]
  :ring {:handler toggler.handler/app}
  :resource-paths ["src/toggler/resources"]
  :main toggler.handler
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
