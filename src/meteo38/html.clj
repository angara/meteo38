(ns meteo38.html
  (:require
   [hiccup2.core :refer [html raw]]
   [meteo38.config :as config]
   [meteo38.icons :refer [map-icon-svg menu-icon-svg]]
  ,))


(defn page-body [_ & content]
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
          [:link {:rel "stylesheet" :href (str config/ASSETS_URI "style.css")}]]
         "\n"
         [:script 
          (raw 
"if(!window.localStorage.getItem('st_list')){
     window.localStorage.setItem('st_list', 'irgp,uiii,csscl,polyana,npsd,botanika7,istok,olha,khomutovo,pivovar_perv');};")
          ]
         "\n"
         [:body {:style "min-height: 100vh;"}
          content]]
        )))


(defn layout [page-body]
  (html
   [:div.flex.flex-col.min-h-screen
    [:header.bg-indigo-100
     [:div.max-w-5xl.mx-auto.px-4.flex.flex-wrap
      [:a.m-1.shrink-0 {:href config/METEO38_URL}
       [:img {:src (str config/ASSETS_URI "meteo38_240x70.png") 
              :style "width:240px; height:70px;"
              :alt "meteo38 logo"}]]

      [:div.flex.grow.items-center.justify-end
       [:a.inline-block.cursor-pointer.text-blue-700.my-3.mx-4 
        {:style "width:28px; height:28px;" :class "hover:text-blue-500"
         :href "/map"}
        (raw map-icon-svg)]
       [:a.inline-block.cursor-pointer.text-blue-700.my-3 
        {:style "width:32px; height:32px;" :onclick "display_options_block();" :class "hover:text-blue-500"} 
        (raw menu-icon-svg)
        ]
       ]
      ]
     ]

    [:div.mx-auto.px-2.py-1.grow {:class "w-full sm:w-[80%] max-w-[700px]"}
      [:div {:id "options_block" :class "transition-all ease-in-out"}]
      page-body
     ]

    [:footer.bg-indigo-100
     [:div.max-w-5xl.mx-auto.px-4.py-4.flex.items-center
      [:div.grow.flex.items-center.gap-x-1
       [:img.inline-block.mr-1 {:src (str config/ASSETS_URI "telegram_logo.svg") :alt "Telegram" :style "height:24px; width:24px;"}]
       [:a.text-blue-700.inline-block {:href "https://t.me/meteo38bot" :target "_blank" :class "pt-[2px]"} "@Mete38bot"] 
       " / "
       [:a.text-blue-700.inline-block {:href "https://t.me/meteo38" :target "_blank" :class "pt-[2px]"} "support"]
       ]
      [:div.text-right
       [:a.text-blue-700 {:href "https://github.com/angara/meteo38" :target "_blank" :title "Meteo38 source code"} 
        [:span.inline-block {:class "pt-[2px]"} "source code"]
        [:img.inline-block.ml-2 {:src (str config/ASSETS_URI "github-mark.svg") :alt "GitHub" :style "height:24px; width:24px;"}]]
       ]
      ]]]
    [:script {:src (str config/ASSETS_URI "htmx.min.js")}]
    [:script {:src (str config/ASSETS_URI "app.js")}]
    "\n"
    (raw (slurp (str config/ASSETS_DIR "counter.html")))
   ))
