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
    [:div.max-w-5xl.mx-auto.px-4.flex
     [:a.m-1 {:href config/METEO38_URL}
      [:img {:src (str config/ASSETS_URI "meteo38_240x70.png") 
             :style "width:240px; height:70px;"
             :alt "meteo38 logo"}]]
     [:div.px-2.flex.grow.items-center.gap-2
      (options-block)
      ;; https://htmx.org/api/#ajax
      [:button {:hx-get "/options" :hx-trigger "click" :hx-target "#options-block" :hx-swap "outerHTML"
                :type "button" 
                :class "inline-block px-6 py-2.5 bg-blue-600 text-white font-medium text-xs rounded shadow-md 
                        hover:bg-blue-700 hover:shadow-lg 
                        focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 
                        active:shadow-lg transition duration-150 ease-in-out"
                ;; leading-tight
                }
        "Options"
       ]
       [:button {:hx-get "/data" :hx-trigger "click" :hx-target "#data-block" :hx-swap "outerHTML"
                :type "button"
                :class "inline-block px-6 py-2.5 bg-blue-600 text-white font-medium text-xs rounded shadow-md 
                        hover:bg-blue-700 hover:shadow-lg 
                        focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 
                        active:shadow-lg transition duration-150 ease-in-out"
                ;; leading-tight
                }
         "Reload"]
      
      ]
      [:div.flex.items-center
        [:a.inline-block.cursor-pointer.text-blue-400 {:style "width:32px; height:32px;" :onclick "display_options_block();"} 
          (raw (slurp (str config/ASSETS_DIR "gear-icon.svg")))]
      ]
     ]
    ]
    [:div.mx-auto.px-4
     page-body
     ]
   [:div.bg-indigo-100
    [:div.max-w-5xl.mx-auto.px-4.py-4.flex.items-center
      [:div.grow.flex.items-center.gap-x-1
       [:img.inline-block {:src (str config/ASSETS_URI "telegram_logo.svg") :alt "Telegram" :style "height:24px; width:24px;"}]
       [:a.text-blue-700.inline-block {:href "https://t.me/meteo38bot" :target "_blank" :class "pt-[2px]"} "@Mete38bot"] 
       " / "
       [:a.text-blue-700.inline-block {:href "https://t.me/meteo38" :target "_blank" :class "pt-[2px]"} "support chat"]
       ]
     [:div.text-right
       [:a.text-blue-700 {:href "https://github.com/angara/meteo38" :target "_blank" :title "Meteo38 source code"} 
        [:span.inline-block {:class "pt-[2px]"} "source code"]
        [:img.inline-block.mx-2 {:src (str config/ASSETS_URI "github-mark.svg") :alt "GitHub" :style "height:24px; width:24px;"}]]
      ]
     ]]
    [:script {:src (str config/ASSETS_URI "htmx.min.js")}]
    [:script {:src (str config/ASSETS_URI "app.js")}]
   ))
