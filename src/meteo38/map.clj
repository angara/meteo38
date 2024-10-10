(ns meteo38.map
   (:require
     [hiccup2.core :refer [html raw]]
     [meteo38.config :as config]
   ,))


(defn map-body [_]
  (str (raw "<!DOCTYPE html>\n")
       (html
        [:html
         [:head
          [:meta {:charset "utf-8"}]
          [:title "Meteo38: Погода в реальном времени"]
          "\n"
          [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]
          [:meta {:name "description" :content "Погода в Иркутске и области в реальном времени"}]
          [:link {:rel "shortcut icon" :href (str config/ASSETS_URI "favicon.ico")}]
          [:link {:rel "stylesheet" :href (str config/ASSETS_URI "ol.css")}]]
          [:link {:rel "stylesheet" :href (str config/ASSETS_URI "map.css")}]]
         "\n"
         [:script {:type "module" :src (str config/ASSETS_URI "map.min.js")}]
         [:body {:style "min-height: 100vh;"}
          [:div {:id "map"}]
          (raw (slurp (str config/ASSETS_DIR "counter.html")))
         ])))

