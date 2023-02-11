(ns meteo38.data
  (:require
    [clojure.string :as str]
    [cheshire.core :as json]
    [org.httpkit.client :as http]
    [meteo38.config :refer [METEO_API_URL API_TIMEOUT]]
   ))



;; NOTE: implement st_data cache

(defn fetch-st-data [st-list]
  (-> (str METEO_API_URL "/st/info?st=" (str/join "," st-list))
      #_{:clj-kondo/ignore [:unresolved-var]}
      (http/get {:timeout API_TIMEOUT})
      (deref)
      (:body)
      (json/parse-string true)
      (:data)
   ))


(defn fetch-st-data-map [st-list]
  (->> st-list
       (fetch-st-data)
       (map #(vector (:id %) %))
       (into {})
       ))


(comment
  
  (fetch-st-data ["uiii" "npsd"])
  ;; => [{:addr "пгт. Маркова, мрн Николов Посад",
  ;;      :elev 560,
  ;;      :id "npsd",
  ;;      :last {:p 969.01, :st "npsd", :t -26.8, :ts "2023-01-21T15:18:19.507+08:00"},
  ;;      :ll [104.2486 52.228],
  ;;      :title "Николов Посад",
  ;;      :trends {:p {:avg 968.1824999999999, :last 968.84},
  ;;               :t {:avg -26.78333333333333, :last -26.9},
  ;;               :ts "2023-01-21T15:12:50.005+08:00"},
  ;;      :ts "2023-01-21T15:18:19.511+08:00"}
  
  ;;     {:addr "г. Иркутск, ул. Ширямова, 101",
  ;;      :elev 495,
  ;;      :id "uiii",
  ;;      :last {:b 320,
  ;;             :cl [4 300],
  ;;             :d -32,
  ;;             :icao "UIII",
  ;;             :p 971,
  ;;             :q 1031,
  ;;             :st "uiii",
  ;;             :t -28,
  ;;             :ts "2023-01-21T15:00:00.000+08:00",
  ;;             :unk ["R30/490242"],
  ;;             :vis 6000,
  ;;             :w 4},
  ;;      :ll [104.366972 52.267288],
  ;;      :title "Иркутский аэропорт",
  ;;      :trends
  ;;        {:p {:avg 970.25, :last 971}, :t {:avg -28.5, :last -28}, :ts "2023-01-21T15:12:50.005+08:00"},
  ;;      :ts "2023-01-21T15:18:00.944+08:00"}]
  
  ,)
