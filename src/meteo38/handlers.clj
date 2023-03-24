(ns meteo38.handlers
  (:require
    [clojure.math :as math]
    [clojure.string :as str]
    [hiccup2.core :refer [html raw]]
    [meteo38.config :refer [ST_LIST_DEFAULT]]
    [meteo38.html :refer [page-body layout]]
    [meteo38.util :refer [html-resp split-st-list]]
    [meteo38.data :refer [fetch-st-data-map]]
    [meteo38.options :refer [options-block]]
   ))


(defn st-graph-ref [st]
  (str "https://angara.net/meteo/st/" st))


(defn- safe-round [v]
  (when (number? v) 
    (math/round v)))


(defn- trend-direction [{:keys [avg last]} delta]
  (when (and (number? avg) (number? last))
    (cond 
      (> last (+ avg delta)) :up 
      (< last (- avg delta)) :down
      :else nil)
    ))


(defn- arrange-by [st-list data-map]
  (->> st-list
       (map #(get data-map %))
       (remove nil?)
       (seq)
       ))


(defn- format-t [t t-dir]
  (when-let [t (safe-round t)]
    (let [[cls value] (cond 
                       (< 0 t) ["text-red-700"  (html [:span {:style "margin-right:1px;"} (raw "&plus;")] t)]
                       (> 0 t) ["text-blue-700" (html [:span {:style "margin-right:1px;"} (raw "&minus;")] (- t))]
                       :else   ["" (raw "&nbsp;0")]
                       )]
      [:div.text-2xl.whitespace-nowrap
       [:span {:class cls} value] 
       [:span.text-gray-400 (raw "&deg;")]
       (case t-dir
          :up   [:span.text-xl.text-red-700  (raw "&uarr;")]
          :down [:span.text-xl.text-blue-700 (raw "&darr;")]
          nil
         )
       ])
    ))


(defn- format-p [p p-dir]
  (let [mmhg (math/round (/ p 1.3332239))]
    [:div.text-green-700.whitespace-nowrap mmhg " мм "
     (case p-dir
       :up   [:span.text-gray-400 (raw "&uarr;")]
       :down [:span.text-gray-400 (raw "&darr;")]
       nil)
    ]))


(defn- format-w [w g b]
  (let [w (safe-round w)
        g (safe-round g)
        dir (when (number? b)
              (get ["С","СВ","В","ЮВ","Ю","ЮЗ","З","СЗ"] (math/round (/ (+ b 22) 45))))
        ]
    [:div.text-blue-800.whitespace-nowrap
     w 
     (when (and g (not= w g)) 
       (str "-" g))
     " м/с"
     (when (and (> w 0) dir) 
       (str ", " dir))
     ]
    ))


(defn data-block [{{raw-st-list :st_list} :params}]
  (let [st-list (or (split-st-list raw-st-list) ST_LIST_DEFAULT)
        st-data (arrange-by st-list (fetch-st-data-map st-list)) 
        ]
    (html 
     [:ul#data-block.my-3.w-full
        (when-not st-data
          [:div.p-12.text-xl.text-indigo-900.text-center
            "Нет актуальных данных по выбранным станциям."])
      
        (for [{:keys [id title addr descr last trends]} st-data   ;; ll elev
              :let [{:keys [p t w g b]} last  ;; ts
                    trend-t (trend-direction (:t trends) 2.0)
                    trend-p (trend-direction (:p trends) 1.0)
                    ; elev (when (number? elev) (math/round elev))
                    ]
              ]
          
          [:li.my-2.px-3.py-1.bg-gray-50.rounded-lg.border.border-slate-100.flex
           [:div.grow.pr-2
            [:div.text-xl.tracking-wide.text-indigo-800 
              [:a {:href (st-graph-ref id) :target "_blank"} title ]
             ]
            (when-not (str/blank? addr)
              [:div.text-gray-600 addr])
            (when-not (str/blank? descr)
              [:div.text-gray-600 descr])
            ; [:div.text-xs.text-gray-500.mt-1 "- " id " (" elev ") " ts]
           ]
           [:div.tracking-wide.text-right.pl-2
            (when (number? t)
              (format-t t trend-t))
            (when (number? p)
              (format-p p trend-p))
            (when (number? w)
              (format-w w g b))
            ]
           ]
          )]
     )))


(defn options [_req]
  (-> 
   (options-block)
   (html)
   (html-resp)
   ))


(defn root-page [{{raw-st-list :st_list} :params :as req}]
  (let [; st-list (split-st-list raw-st-list)
        ; st-data (fetch-st-data st-list)
        ; XXX nil
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
