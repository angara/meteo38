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

;; ???
(def API_METEO_URL 
  (-> (System/getenv "API_METEO_URL") 
      (or "https://angara.net/api/meteo")))

(def METEO_API_URL
  (-> (System/getenv "METEO_API_URL")
      (or "http://rs.angara.net/meteo/api")))
