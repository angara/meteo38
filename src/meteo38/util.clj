(ns meteo38.util
  (:import 
   [java.time Instant ZoneId ZoneOffset]
   [java.time.format DateTimeFormatter]
   )
  (:require
    [clojure.string :as str]
    [cheshire.core :as json]
   ,))


(set! *warn-on-reflection* true)


(defn split-st-list [s]
  (when (string? s)
    (->> (str/split s #",")
         (remove str/blank?)
         (seq)
        )))

(comment
  
  (split-st-list ",,uiii,npsd,ubil31")
  ;; => ("uiii" "npsd" "ubil31")
  
  (split-st-list "")
  ;; => nil
 
  ,)


(def FMT_dmy_hm
  (-> "dd.MM.yyyy - HH:mm"
      (DateTimeFormatter/ofPattern )
      (.withZone (ZoneId/systemDefault))))


(defn now-inst ^Instant []
  (java.time.Instant/now))


(defn format-dmy-hm [^Instant dt]
  (.format ^DateTimeFormatter FMT_dmy_hm dt))

(comment
  (format-dmy-hm (now-inst))
  ;;=> "03.01.2025 13:59"
  ,)


(defn instant->rfc1123 [^java.time.Instant instant]
  (->>
   (.atOffset instant ZoneOffset/UTC)
   (.format DateTimeFormatter/RFC_1123_DATE_TIME)))


(defn html-resp [body]
  {:status 200
   :headers {"Content-Type" "text/html;charset=utf-8"}
   :body (str body)
   })


(defn json-resp [data]
  {:status 200
   :headers {"Content-Type" "application/json;charset=utf-8"}
   :body (json/generate-string data)
   })


(defn parse-query-string [qs]
  (when-not (str/blank? qs)
    (->> (str/split qs #"&")
         (map  #(let [[k v] (str/split % #"=")]
                  [(keyword k) v]))
         (into {}))))

(comment

  (parse-query-string nil)
  ;; => nil

  (parse-query-string "")
  ;; => nil

  (parse-query-string "&")
  ;; => {}

  (parse-query-string "a=a&a=b&c=1&d=&e")
  ;; => {:a "b", :c "1", :d nil, :e nil}
  )


(defn wrap-query-params [handler]
  (fn [req]
    (let [query-params (-> req (:query-string) (parse-query-string))]
      (handler (update req :params merge query-params)))))

