(ns meteo38.exp-tjs
  (:require 
    [clojure.math :refer [round]]
    [meteo38.data :refer [fetch-st-data]]
   ,))


(defn- format-t [st t]
  (when (number? t)
    (let [t (round t)
          clr (cond 
                (> t 0) "#a40"
                (< t 0) "#04d"
                :else   "#555")
          link  (str "<a href=\"https://meteo38.ru/\" style=\"color:" clr ";text-decoration:none;\">"
                      (when (> t 0) "+") t "&deg;</a>")
          ]
        (str "try{document.getElementById(\"meteo38_t_" st "\").innerHTML='" link "';}catch(err){};")
      )))


(defn tjs [{{st :st} :params}]
  (let [t    (-> (fetch-st-data [st]) (first) :last :t)
        body (or (format-t st t) "")
        ]
    {:status 200
     :headers {"Content-Type" "text/javascript"
               "Cache-Control" "no-cache, no-store, must-revalidate"
               "Pragma" "no-cache"
               "Expires" "0"}
     :body body}
    ))

(comment
  
  (tjs {:params {:st "uiii"}})
  ;;=> {:body
  ;;      "try{document.getElementById(\"meteo38_t_uiii\").innerHTML='<a href=\"https://meteo38.ru/\" style=\"color:#04d;text-decoration:none;\">-2&deg;</a>';}catch(err){};",
  ;;    :headers {"Cache-Control" "no-cache, no-store, must-revalidate",
  ;;              "Content-Type" "text/javascript",
  ;;              "Expires" "0",
  ;;              "Pragma" "no-cache"},
  ;;    :status 200}
  
  ,)