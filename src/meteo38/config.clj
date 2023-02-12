(ns meteo38.config)


(def ASSETS_DIR "./public/assets/")
(def ASSETS_URI "/assets/")

(def METEO38_URL "https://meteo38.ru/")

(def API_TIMEOUT 5000)  ;; ms


(def HTTP_SERVER_HOST 
  (-> (System/getenv "HTTP_SERVER_HOST") 
      (or "localhost")))

(def HTTP_SERVER_PORT 
  (-> (System/getenv "HTTP_SERVER_PORT") 
      (or "8038") 
      (Integer/parseInt)))

(def METEO_API_URL 
  (-> (System/getenv "METEO_API_URL") 
      (or "https://angara.net/api/meteo")))


(def ST_LIST_DEFAULT 
  '("irgp" "soln" "uiii" "istok"  "npsd" "olha" "khomutovo" "vtsg"))
