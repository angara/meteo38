(ns meteo38.handlers
  (:require
    [hiccup2.core :refer [html]]
    [meteo38.config :refer [ASSETS_URI METEO38_URL ST_LIST_DEFAULT]]
    [meteo38.html :refer [page-body layout]]
    [meteo38.util :refer [html-resp split-st-list]]
    [meteo38.data :refer [fetch-st-data fetch-st-data-map]]
   ))


(defn- arrange-by [st-list data-map]
  (map #(get data-map %) st-list))


(defn data-block [{{raw-st-list :st_list} :params}]
  (let [st-list (or (split-st-list raw-st-list) ST_LIST_DEFAULT)
        st-data (arrange-by st-list (fetch-st-data-map st-list))]
    (prn "st-data:" st-data)
    (html 
     [:ul 
        (for [{:keys [id title addr descr last trends _ll _elev]} st-data
              :let [_ts (:ts last)]
              ]
          [:li 
           [:div id " - " title]
           [:div addr]
           [:div descr]
           [:div (str last)]
           [:div (str trends)]
           
           ]
          
          )]
     )))


(defn root-page [{{raw-st-list :st_list} :params :as req}]
  (let [; st-list (split-st-list raw-st-list)
        ; st-data (fetch-st-data st-list)
        XXX nil
        ]
    (html-resp
     (page-body req
                (layout

                  (data-block req)
                 )
                ))))


(defn data-page [req]
  (html-resp (data-block req)))


(comment
  
  (root-page {:uri "/"})
  
  ,)
