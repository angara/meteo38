(ns meteo38.handlers
  (:require
    [meteo38.const :refer [ASSETS_URI]]
    [meteo38.util :refer [html-resp page-body]]
   ))


(defn root-page [{:keys [_config] :as _req}]
  (html-resp
    (page-body 
      [:img {:src (str ASSETS_URI "meteo38_240x70.png") :alt "meteo38 logo"}]
      [:h1 "test of meteo38"]
      [:h2 "subtitle"]
     )))


(defn data-block [{:keys [_config] :as _req}]

  (html-resp [:ul [:li "part of list"]])  
  )


(comment
  
  (root-page {})
  
  
  ,)