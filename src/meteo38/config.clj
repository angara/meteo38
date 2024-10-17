(ns meteo38.config)


(def ASSETS_DIR "./public/assets/")
(def ASSETS_URI "/assets/")

(def METEO38_URL "https://meteo38.ru/")


(def HTTP_SERVER_HOST 
  (-> (System/getenv "HTTP_SERVER_HOST") 
      (or "localhost")))

(def HTTP_SERVER_PORT 
  (-> (System/getenv "HTTP_SERVER_PORT") 
      (or "8038") 
      (Integer/parseInt)))


(def API_TIMEOUT 5000)  ;; ms

(def METEO_API_URL
  (-> (System/getenv "METEO_API_URL")
      (or "http://rs.angara.net/meteo/api")))

(def METEO_API_AUTH
  (str "Basic " (System/getenv "BASIC_AUTH")))
