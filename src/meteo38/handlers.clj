(ns meteo38.handlers
  (:require
    [meteo38.util :refer [html-resp page-body]])
  )



(defn root-page [{:keys [_config] :as _req}]
  (html-resp
    (page-body [:h1 "test of meteo38"])))


(defn data-block [{:keys [_config] :as _req}]

  (html-resp [:ul [:li "part of list"]])  
  )


(comment
  
  (root-page {})
  
  
  ,)