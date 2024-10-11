(ns meteo38.data
  (:import 
   [java.time Clock ZonedDateTime Instant]
   [java.time.temporal ChronoUnit]
   [java.time.format DateTimeFormatter])
  (:require
    [clojure.string :as str]
    [cheshire.core :as json]
    [org.httpkit.client :as http]
    [meteo38.config :refer [METEO_API_URL METEO_API_AUTH API_TIMEOUT]]
   ,))


(set! *warn-on-reflection* true)


(defn iso->milli [^String dt]
  (try
    (-> (.parse DateTimeFormatter/ISO_DATE_TIME dt)
        (Instant/from)
        (.toEpochMilli))
    (catch Exception _ignore)))


(comment
  
  (iso->milli "2023-04-01T15:26:00.507+08:00")
  ;; => 1680333960507

  ,)


;; NOTE: implement st_data cache

(def ^:const FRESH_OFFSET (* 80 60 1000)) ;; 80 minutes

(defn- is-fresh-ts [ts]
  (when-let [ts (iso->milli ts)]
    (> ts (- (System/currentTimeMillis) FRESH_OFFSET))))


(defn set-fresh-tpw [st-data]
  (cond-> st-data
    (is-fresh-ts (-> st-data :last :t_ts)) (assoc-in [:last :t_fresh] true)
    (is-fresh-ts (-> st-data :last :p_ts)) (assoc-in [:last :p_fresh] true)
    (is-fresh-ts (-> st-data :last :w_ts)) (assoc-in [:last :w_fresh] true)
    ,))


(def PRIORITY_LETTERS 
  #{\А \Б \В \Г \Д \Е \Ë \Ж \З \И \Й \К \Л \М \Н \О \П \Р \С \Т \У \Ф \Х \Ц \Ч \Ш \Щ \Ъ \Ы \Ь \Э \Ю \Я
    \а \б \в \г \д \е \ё \ж \з \и \й \к \л \м \н \о \п \р \с \т \у \ф \х \ц \ч \ш \щ \ъ \ы \ь \э \ю \я})


(defn station-title-sort [{title :title}]
  (if (PRIORITY_LETTERS (first title))
    (str "0" title)
    (str "1" title)))


(defn active-stations []
  (-> (str METEO_API_URL "/active-stations?last-hours=4")
      #_{:clj-kondo/ignore [:unresolved-var]}
      (http/get {:headers {"Authorization" METEO_API_AUTH} :timeout API_TIMEOUT})
      (deref)
      (:body)
      (json/parse-string true)
      (:stations)
      ,))


(def STATION_FETCH_INTERVAL (* 1000 200))  ;; 200 secs

(def station-cache_ (atom {:expire 0 :data nil}))


(defn station-list []
  (let [{:keys [expire data]} @station-cache_
        now (System/currentTimeMillis)]
    (if (> expire now)
      data
      (let [data (->> (active-stations) (sort-by station-title-sort))
            expire (+ now STATION_FETCH_INTERVAL)]
        (reset! station-cache_ {:expire expire :data data})
        data))))


(defn fetch-st-data [st-list]
  (let [flt (set st-list)]
    (->> (station-list)
         (filter #(flt (:st %)))
         (map set-fresh-tpw)
         )))

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

;; TODO: implement cache
(defn station-hourly [st t0]
  (-> (str METEO_API_URL "/station-hourly?st=" st "&ts-beg=" t0)
      #_{:clj-kondo/ignore [:unresolved-var]}
      (http/get {:timeout API_TIMEOUT})
      (deref)
      (:body)
      (json/parse-string true)
      ,))


(defn st-hourly [st ^long hours]
  (let [t1 (ZonedDateTime/now (Clock/systemUTC))
        t0 (.minus t1 hours ChronoUnit/HOURS)
        data (station-hourly st (str t0))]
    (:series data)
    ,))


(comment

  (st-hourly "uiii" 10)
  ;;=> {:d [0.0 1.5 -2.0 -2.0 -2.0 -2.0 -2.0 -2.0 -2.0 -1.5 -1.0],
  ;;    :p [959.0 957.5 956.0 955.0 954.5 955.0 955.0 956.0 956.5 957.0 958.0],
  ;;    :t [0.0 2.0 5.5 6.0 7.0 7.0 6.5 6.0 5.0 5.0 5.0],
  ;;    :w [1.0 1.0 2.0 1.5 1.0 1.0 3.0 3.5 3.0 2.5 2.5]}

  (st-hourly "npsd" 10)
  ;;=> {:p [954.3683304565 954.0827983379166 953.7117176857499 952.7511298657998 952.7095777209165
  ;;        952.9573351623332 953.5861724351663 954.3472210780834 955.0393864861666 955.7348849539999
  ;;        955.974865256],
  ;;    :t [-0.375 1.6499999999999997 3.875 5.2 6.116666666666667 6.341666666666666 6.041666666666667
  ;;        5.416666666666667 4.7666666666666675 4.433333333333333 4.112499999999999]}

  (->>
   (station-list)
   (map :title))
  ;;=> ("Абакан"
  ;;    "Аршан"
  ;;    "Аэропорт Анталья"
  ;;    "Аэропорт Владивосток (VVO)"
  ;;    "Аэропорт Манас"
  ;;    "Аэропорт Шереметьево (SVO)"
  ;;    "Байкальск"
  ;;    "Ботаника 7"
  ;;    "Братский аэропорт"
  ;;    "Быстрая"
  ;;    "Голоустное"
  ;;    "Гражданпроект"
  ;;    "Иркутский аэропорт"
  ;;    "Красноярск"
  ;;    "Култук"
  ;;    "Минводы"
  ;;    "Мирный"
  ;;    "Николов Посад"
  ;;    "Новокузнецк"
  ;;    "Новостройка"
  ;;    "Олха (верх)"
  ;;    "Пивовариха"
  ;;    "Пик Любви"
  ;;    "Самарта"
  ;;    "Сарминский голец"
  ;;    "Саянск, Юбилейный, 67"
  ;;    "Соболиная"
  ;;    "Тунка"
  ;;    "Узуры"
  ;;    "Улан-Батор"
  ;;    "Улан-Удэ, аэропорт"
  ;;    "Хадарта"
  ;;    "Хайта"
  ;;    "Хомутово"
  ;;    "Хужир"
  ;;    "Чита"
  ;;    "Юлинск"
  ;;    "Якутск"
  ;;    "Яхт-клуб Исток"
  ;;    "Krabi airport (KBV)"
  ;;    "Phuket airport (HKT)")
  
 (-> ["uiii" "npsd"]
     (fetch-st-data))
 ;;=> ({:created_at "2004-10-10T21:00:00+09:00",
 ;;     :descr "г. Иркутск, ул. Ширямова, 101",
 ;;     :elev 495.0,
 ;;     :last {:b 360.0,
 ;;            :b_ts "2024-10-11T09:30:00+08:00",
 ;;            :d -2.0,
 ;;            :d_delta 1.0,
 ;;            :d_ts "2024-10-11T09:30:00+08:00",
 ;;            :p 960.0,
 ;;            :p_delta 0.0,
 ;;            :p_ts "2024-10-11T09:30:00+08:00",
 ;;            :t -2.0,
 ;;            :t_delta 1.0,
 ;;            :t_ts "2024-10-11T09:30:00+08:00",
 ;;            :w 1.0,
 ;;            :w_delta -0.5,
 ;;            :w_ts "2024-10-11T09:30:00+08:00"},
 ;;     :last_ts "2024-10-11T09:30:00+08:00",
 ;;     :lat 52.267288,
 ;;     :lon 104.366972,
 ;;     :publ true,
 ;;     :st "uiii",
 ;;     :title "Иркутский аэропорт"}
 ;;    {:created_at "2013-02-17T15:40:04.648+09:00",
 ;;     :descr "пгт. Маркова, мрн Николов Посад",
 ;;     :elev 560.0,
 ;;     :last {:p 955.1482664379998,
 ;;            :p_delta 0.07999343399990266,
 ;;            :p_ts "2024-10-11T09:41:35.968434+08:00",
 ;;            :t -1.6,
 ;;            :t_delta 0.47499999999999964,
 ;;            :t_ts "2024-10-11T09:41:35.968434+08:00"},
 ;;     :last_ts "2024-10-11T09:41:35.968434+08:00",
 ;;     :lat 52.228,
 ;;     :lon 104.2486,
 ;;     :publ true,
 ;;     :st "npsd",
 ;;     :title "Николов Посад"})
    
  ,)
