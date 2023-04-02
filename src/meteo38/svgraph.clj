(ns meteo38.svgraph
  (:require
    [hiccup2.core :refer [html]]
    [meteo38.data :refer [st-hourly]]
   ))


(def HOURS 10)


(defn render [st]
  (let [data-t (->> (st-hourly st HOURS) 
                    (mapv #(-> % :t :avg)))]

    ; (prn "svg render:" st data-t)
    
    (html
     ;[:div (str data-t)]
     [:div ]
     ; [:svg]
     ))
  )


(comment
  
  (render "uiii")
  
  ,)