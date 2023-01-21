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


(comment
  
  (fetch-st-data ["uiii" "npsd"])
  
  ,)
