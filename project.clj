(defproject toggler "1.1.5"
  :description "Toggler - A Feature Toggle Service written in Clojure"
  :url "https://github.com/maleghast/toggler"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [liberator "0.15.3"]
                 [compojure "1.7.0"]
                 [cheshire "5.12.0"]
                 [ring "1.10.0"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler toggler.handler/app}
  :resource-paths ["src/toggler/resources"]
  :main toggler.handler
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
