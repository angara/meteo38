(ns meteo38.util
  (:require
    [hiccup2.core :refer [html raw]]
   ))


(set! *warn-on-reflection* true)


(defn instant->rfc1123 [^java.time.Instant instant]
  (->>
   (.atOffset instant java.time.ZoneOffset/UTC)
   (.format java.time.format.DateTimeFormatter/RFC_1123_DATE_TIME)))


(defn html-resp [body]
  {:status 200
   :headers {"content-type" "text/html;charset=utf-8"}
   :body (str body)
   })


(defn page-body [& content]
  (str (raw "<!DOCTYPE html>\n")
       (html
        [:html
         [:head
           [:meta {:charset "utf-8"}]
           [:title "Meteo38"] "\n"
           [:script (raw (slurp "./public/assets/local_redir.js"))]
           [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]
           [:meta {:name "description" :content "Погода в Иркутске и области в реальном времени"}]
           [:link {:rel "shortcut icon" :href "/assets/favicon.ico"}] 
           [:link {:rel "stylesheet" :href "/assets/style.css"}]
         ] 
         [:body
          content
          ]])))
