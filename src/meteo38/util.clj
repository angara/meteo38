(ns meteo38.util
  (:require
    [hiccup2.core :refer [html raw]]
   ))


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
          [:title "Meteo38"]]
         [:body
          content
          ]])))
