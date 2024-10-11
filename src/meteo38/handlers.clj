(ns meteo38.handlers
  (:require
    [clojure.math :as math]
    [clojure.string :as str]
    [hiccup2.core :refer [html raw]]
    [meteo38.html :refer [page-body layout]]
    [meteo38.map  :refer [map-body]]
    [meteo38.util :refer [json-resp html-resp split-st-list]]
    [meteo38.data :refer [fetch-st-data station-list]]
    [meteo38.options :refer [options-block]]
    [meteo38.icons :as ico]
    [meteo38.svgraph :as svgraph]
   ,))


(defn permalink-st-list [st-list]
  (str "https://meteo38.ru/?st_list=" 
       (->> st-list (map :st) (str/join ","))))


(defn st-graph-ref [st]
  (str "https://angara.net/meteo/st/" st))


(defn- safe-round [v]
  (when (number? v) 
    (math/round v)))


(defn- trend-direction [delta interval]
  (when (and (number? delta) (number? interval))
    (cond 
      (> delta interval) :up 
      (< delta (- interval)) :down
      :else nil)
    ))


(defn- arrange-by [st-list data-list]
  (let [data-map (into {} (map #(vector (:st %) %)) data-list)]
    (->> st-list
         (map #(get data-map %))
         (remove nil?)
         (seq)
         )))


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


(defn format-data-item [{:keys [st title descr last elev lat lon]}]  ;; st-data
  (let [{:keys [p t w g b]} last  ;; ts
        trend-t (trend-direction (:t_delta last) 2.0)
        trend-p (trend-direction (:p_delta last) 1.0)
        elev (when (number? elev) (math/round elev))
        note (str st " - lat:" lat ", lon:" lon ", elev:" elev)]
    ;
    [:li.my-2.px-3.py-1.bg-gray-50.rounded-lg.border.border-slate-100.flex
     {:onclick "st_item_click(this)" :data-st st}
     [:div.grow.pr-2
      [:div.text-xl.tracking-wide.text-indigo-800
       [:a {:href (st-graph-ref st) :target "_blank" :title note} title]]
      (when-not (str/blank? descr)
        [:div.text-gray-600 descr])
      [:div {:id (str "svgraph_" st)}]]
     [:div.tracking-wide.text-right.pl-2
      (when (number? t)
        (format-t t trend-t))
      (when (number? p)
        (format-p p trend-p))
      (when (number? w)
        (format-w w g b))]]
    ))


(defn data-block [{{raw-st-list :st_list} :params}]
  (let [st-list (split-st-list raw-st-list)
        st-data-list (arrange-by st-list (fetch-st-data st-list)) 
        ]
    (html 
     [:div#data-block.my-2.w-full
      (if st-data-list
        (list
         [:ul
           (for [std st-data-list]
            (format-data-item std))]
         [:div.text-right
          [:a.text-sky-600.inline-block.mx-3.my-0 
           {:href (permalink-st-list st-data-list) :target "_blank"
            :title "Прямая ссылка на выбранные станции"} 
           (raw ico/external-link-svg)]
          ]
         )
        (if raw-st-list
          [:div.p-12.text-xl.text-indigo-900.text-center
           "Нет актуальных данных по выбранным станциям."]
          [:div.text-gray-300.p-2.text-center 
           "Загрузка данных ..."
           [:script (raw "window.initial_load = true;")]]))
      ]
     )))


(defn options [_req]
  (html-resp (options-block)))


(defn root-page [req]
  (html-resp
    (page-body req 
      (layout (data-block req))
    )))


(defn data-page [req]
  (html-resp (data-block req)))


(defn svgraph [{{st :st} :params}]
  (html-resp (svgraph/render st)))


;; ;; ;; ;; ;; ;; ;; ;; ;; ;;


(defn geo-map [req]
  (html-resp (map-body req)))


(defn geo-map-data [_]
  (json-resp {:stations (station-list)}))
