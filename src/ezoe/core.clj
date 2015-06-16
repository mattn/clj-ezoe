(ns ezoe.core
  (:gen-class)
  (:use net.cgrand.enlive-html)
  (:import java.net.URL)
  (:require [feedparser-clj.core :as feedparser]
            [io.aviso.ansi :as ansi]
            [clj-http.client :as client]))

(defn list-items []
  ((def feed (feedparser/parse-feed (str "http://ask.fm/feed/profile/" "EzoeRyou" ".rss")))
  (doseq [entry (:entries feed)]
    (println (ansi/white (:uri entry)))
    (println (str "  " (ansi/blue (:title entry))))
    (let [ask (:value (:description entry))]
      (println (str "  " (clojure.string/replace ask
                #"(質問ではない。?|不?自由)" #(ansi/red (%1 1))))))
    (println))))

;;; FIXME:
(defn post-item [mes]
  (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
    (let [token (-> "http://ask.fm/mattn_jp" URL. html-resource
        (select [(attr= :name "authenticity_token")]) first :attrs :value)]
      (println token)
      (print (client/post "http://ask.fm/mattn_jp/questions/create"
      ;(print (client/post "http://httpbin.org/post"
        {:form-params {
             "authenticity_token" token
             "question[question_text]" mes
             "question[force_anonymous]" "force_anonymous"}
         :headers { "Referer" "http://ask.fm/mattn_jp" }})))))

(defn -main
  "質問ではない。"
  [& args]
  (if (> (count args) 0)
    (post-item (clojure.string/join "" args))
    (list-items)))
