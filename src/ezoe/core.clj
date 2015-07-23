(ns ezoe.core
  (:gen-class)
  (:use net.cgrand.enlive-html)
  (:import java.net.URL)
  (:require [feedparser-clj.core :as feedparser]
            [jansi-clj.auto]
            [jansi-clj.core :refer :all]
            [clj-http.client :as client]))

(defn list-items []
  (def feed (feedparser/parse-feed (str "http://ask.fm/feed/profile/" "EzoeRyou" ".rss")))
  (doseq [entry (:entries feed)]
    (println (:uri entry))
    (println (str "  " (blue (:title entry))))
    (let [ask (:value (:description entry))]
      (println (str "  " (clojure.string/replace ask
                #"(質問ではない。?|不?自由)" #(red (%1 1))))))
    (println)))

(defn parse-html [html]
  (html-resource (java.io.StringReader. html)))

;;; FIXME:
(defn post-item [mes]
  (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
  (let [token (-> (first
    (select (parse-html (get (client/get "http://ask.fm/mattn_jp") :body))
      [(attr= :name "authenticity_token")])) :attrs :value)]
    (println token)
    (print (client/post "http://ask.fm/mattn_jp/questions/create" {
    ;(print (client/post "http://httpbin.org/post" {
      :content-type "application/x-www-form-urlencoded; charset=UTF-8"
      :body (client/generate-query-string {
           "authenticity_token" token
           "question[force_anonymous]" "force_anonymous"
           "question[question_text]" mes
           })
      :headers { "Referer" "http://ask.fm/mattn_jp" }})))))

(defn -main
  "質問ではない。"
  [& args]
  (if (> (count args) 0)
    (post-item (clojure.string/join "" args))
    (list-items)))
