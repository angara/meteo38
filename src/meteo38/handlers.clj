(ns meteo38.handlers
  (:require
    [clojure.math :as math]
    [clojure.string :as str]
    [hiccup2.core :refer [html raw]]
    [meteo38.config :refer [ASSETS_URI METEO38_URL ST_LIST_DEFAULT]]
    [meteo38.html :refer [page-body layout]]
    [meteo38.util :refer [html-resp split-st-list]]
    [meteo38.data :refer [fetch-st-data-map]]
   ))


(defn- trend-direction [{:keys [avg last]}]
  (when (and (number? avg) (number? last))
    (cond 
      (< avg (+ last 0.5)) :up 
      (> avg (- last 0.5)) :down
      :else nil)
    ))


(defn- arrange-by [st-list data-map]
  (map #(get data-map %) st-list))


(defn- format-t [t]
  (when-let [t (and (number? t) (math/round t))]
    (let [[cls value] (cond 
                       (< 0 t) ["text-red-700" (html (raw "&plus;") t)]
                       (> 0 t) ["text-blue-700" (html (raw "&minus;") (- t))]
                       :else   ["" (raw "&nbsp;0")]
                       )]
      [:div.text-xl [:span {:class cls} value] (raw "&deg;")])
    ))


(defn data-block [{{raw-st-list :st_list} :params}]
  (let [st-list (or (split-st-list raw-st-list) ST_LIST_DEFAULT)
        st-data (arrange-by st-list (fetch-st-data-map st-list))]
    (prn "st-data:" st-data)
    (html 
     [:ul.my-2 
        (for [{:keys [id title addr descr last trends _ll _elev]} st-data
              :let [ts (:ts last) 
                    trend-t (trend-direction (:t trends))]
              ]
          [:li.my-2.px-3.py-1.bg-slate-50.rounded-lg.border.border-slate-100.flex
            ; {:class "hover:bg-sky-50 hover:border-sky-100"}
           [:div.grow
            [:div.text-xl.font-semibold.tracking-wide.text-indigo-800 title]
            (when-not (str/blank? addr)
              [:div.text-gray-600 addr])
            (when-not (str/blank? descr)
              [:div.text-gray-600 descr])
            [:div.text-xs.text-gray-500.mt-1 "- " id ": " ts]
           ]
           [:div.tracking-wide.text-right
            [:div (format-t (:t last))]
            ;[:div (str last)]
            [:div trend-t " - " (str trends)]
            ]
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
