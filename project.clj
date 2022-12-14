(defproject simple-http "0.1.0-SNAPSHOT"
  :description "Experiments with Clojure concurrent features in a simple http server"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :repl-options {:init-ns simple-http.core}
  :main ^:skip-aot simple-http.core)
