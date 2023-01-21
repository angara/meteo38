(ns meteo38.util
  (:require
    [clojure.string :as str]
   ))


(set! *warn-on-reflection* true)


(defn split-st-list [s]
  (when (string? s)
    (->> (str/split s #",")
         (remove str/blank?)
        )))

(comment
  
  (split-st-list ",,uiii,npsd,ubil31")
  ;; => ("uiii" "npsd" "ubil31")
  
  ,)


(defn instant->rfc1123 [^java.time.Instant instant]
  (->>
   (.atOffset instant java.time.ZoneOffset/UTC)
   (.format java.time.format.DateTimeFormatter/RFC_1123_DATE_TIME)))


(defn html-resp [body]
  {:status 200
   :headers {"Content-Type" "text/html;charset=utf-8"}
   :body (str body)
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

