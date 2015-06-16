(defproject ezoe "0.1.0-SNAPSHOT"
  :description "質問ではない。"
  :url "https://github.com/mattn/cl-ezoe"
  :license {:name "MIT"
            :url "http://mattn.mit-license.org/2015"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [io.aviso/pretty "0.1.18"]
                 [enlive "1.1.5"]
                 [clj-http "1.1.2"]]
  :main ^:skip-aot ezoe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
