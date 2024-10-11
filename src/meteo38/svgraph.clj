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
(def t-height 30)
(def h-pad 2)
(def svg-height (+ t-height h-pad h-pad))
(def svg-width (+ bar-x0 (* HOURS x-step)))


(defn bound-t [data-t height y0 step]
  (loop [y0 y0 [t & t-rest] data-t]
    (if (nil? t)
      y0
      (recur
        (cond
          (> t (+ y0 height))
          (or (some #(when (<= t (+ % height)) %) (range y0 100 step)) 100)

          (< t y0)
          (or (some #(when (> t %) %) (range y0 -100 (- step))) -100)

          :else y0
          )
        t-rest)
      )))


(defn render [st]
  (let [data-t (->> (st-hourly st HOURS)
                    (:t)
                    (map #(if % (math/round %) 0)
                         )
                    )]
    (if-not (seq data-t)
      (html [:div]) ;; no data
      (let [y0 (bound-t data-t t-height -10 10)]
        (html 
         [:svg {:style "margin:2px;"
                :width (str svg-width) :height (str svg-height)
                :viewBox (str "0 " (- (+ y0 t-height h-pad)) " " (str svg-width) " " (str svg-height)) 
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