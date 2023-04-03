(ns meteo38.svgraph
  (:require
    [hiccup2.core :refer [html]]
    [meteo38.data :refer [st-hourly]]
    [clojure.math :as math]))
   


(def HOURS 48)

(def bar-width 3)
(def bar-gap 2)
(def bar-x0 2)
(def x-step (+ bar-width bar-gap))
(def svg-height 30)
(def svg-width (+ bar-x0 (* HOURS x-step)))


(defn bound-t [[t0 & data-t]]
  (loop [t-min t0 t-max t0 up? true [t & t-rest] data-t]
    (if (nil? t)
      (if up? 
        [(* 10 (math/ceil  (/ t-max 10))) true] 
        [(* 10 (math/floor (/ t-min 10))) false])
      (cond 
        (> t t-max) (recur t-min t true t-rest) 
        (< t t-min) (recur t t-max false t-rest)
        :else       (recur t-min t-max up? t-rest)))))


(defn render [st]
  (let [data-t (->> (st-hourly st HOURS)
                    (keep #(-> % :t :avg)))]
    (if-not (seq data-t)
      (html [:div]) ;; no data
      (let [[t-val up?] (bound-t data-t)
            ; _ (prn "t-val:" t-val up?)
            [view-y0 view-y1] (if up?
                              [(- t-val) (- (- t-val svg-height))]
                              [(- t-val) (- (- t-val svg-height))]
                              )
            ]
        ; (prn "svg-y:" view-y0 view-y1)
        (html 
         [:svg {:style "margin:2px;"
                :width (str svg-width) :height (str svg-height)
                :viewBox (str "0 " view-y0 " " (str svg-width) " " view-y1) 
                :fill "none" :stroke "currentColor" :stroke-linecap "round" :stroke-linejoin "round" :stroke-width (str bar-width)}
          (->> data-t
               (map-indexed (fn [idx t]
                              (let [x (+ bar-x0 (* idx x-step))]
                                (if (< t 0)
                                  [:line {:x1 (str x) :y1 "0" :x2 (str x) :y2 (str (- t)) :stroke "blue"}]
                                  [:line {:x1 (str x) :y1 "0" :x2 (str x) :y2 (str (- t)) :stroke "red"}])))))])))))
                                  
               


(comment

  (render "uiii")
  
  ,)