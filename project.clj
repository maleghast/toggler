(defproject toggler "1.1.4"
  :description "Toggler - A Feature Toggle Service written in Clojure"
  :url "https://github.com/maleghast/toggler"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [liberator "0.14.1"]
                 [compojure "1.5.0"]
                 [cheshire "5.6.1"]
                 [ring "1.5.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler toggler.handler/app}
  :resource-paths ["src/toggler/resources"]
  :main toggler.handler
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
