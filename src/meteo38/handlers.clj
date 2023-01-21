(ns meteo38.handlers
  (:require
    [hiccup2.core :refer [html]]
    [meteo38.config :refer [ASSETS_URI METEO38_URL]]
    [meteo38.html :refer [page-body]]
    [meteo38.util :refer [html-resp]]
   ))


(defn data-block [{:keys [_config] :as _req}]
  (html 
    [:ul [:li "part of list"]]))


(defn root-page [{:keys [_config] :as req}]
  (html-resp
    (page-body req
               [:a {:href METEO38_URL}
                 [:img {:src (str ASSETS_URI "meteo38_240x70.png") :alt "meteo38 logo"}]]
      [:h1 "test of meteo38"]
      [:h2 "subtitle"]
      (data-block req)
      [:div "footer"]
     )))


(defn data-page [req]
  (html-resp (data-block req)))


(comment
  
  (root-page {:uri "/"})
  
  ,)
