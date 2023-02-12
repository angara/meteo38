(ns meteo38.html
  (:require
   [hiccup2.core :refer [html raw]]
   [meteo38.config :as config]
   [meteo38.options :refer [options-block]]
   ))


(defn page-body [{uri :uri params :params} & content]
  (str (raw "<!DOCTYPE html>\n")
       (html
        [:html
         [:head
          [:meta {:charset "utf-8"}]
          [:title "Meteo38"] 
          "\n"
          (when (and (= "/" uri) (not (:st_list params)))
            [:script (raw (slurp (str config/ASSETS_DIR "local_redir.js")))])
          "\n"
          [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]
          [:meta {:name "description" :content "Погода в Иркутске и области в реальном времени"}]
          [:link {:rel "shortcut icon" :href (str config/ASSETS_URI "favicon.ico")}]
          [:link {:rel "stylesheet" :href (str config/ASSETS_URI "style.css")}]]
         "\n"
         [:body
          content]]
        )))


(defn layout [page-body]
  (html
   [:div.bg-indigo-100
    [:div.container.mx-auto.px-4.flex
     [:a.m-1 {:href config/METEO38_URL}
      [:img {:src (str config/ASSETS_URI "meteo38_240x70.png") 
             :style "width:240px; height:70px;"
             :alt "meteo38 logo"}]]
     [:div.px-2
      "header "
      "XXX: !!!"
      (options-block {})
      ]
     ]
    ]
    [:div.container.mx-auto.px-4
     page-body
     ]
   [:div.bg-indigo-100
    [:div.container.mx-auto.px-4.py-2.flex
      [:div.grow.flex.items-center
       [:a.text-blue-700 {:href "https://t.me/meteo38bot" :target "_blank" :style "padding-top: 1px;"} "@Mete38bot"] 
       [:img.inline-block.mx-2 {:src (str config/ASSETS_URI "telegram_logo.svg") :alt "Telegram" :style "height:24px; width:24px;"}]
       [:a.text-blue-700 {:href "https://t.me/meteo38" :target "_blank" :style "padding-top: 1px;"} "Chat"]
       ]
     [:div.text-right
       [:a.text-blue-700 {:href "https://github.com/angara/meteo38" :target "_blank"} 
        [:img.inline-block.mx-2 {:src (str config/ASSETS_URI "github-mark.svg") :alt "GitHub" :style "height:24px; width:24px;"}]]
      ]
     ]]
   ))
