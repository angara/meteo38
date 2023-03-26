(ns meteo38.data
  (:import 
   [java.time Clock ZonedDateTime]
   [java.time.temporal ChronoUnit]
   )
  (:require
    [clojure.string :as str]
    [cheshire.core :as json]
    [org.httpkit.client :as http]
    [meteo38.config :refer [METEO_API_URL API_TIMEOUT]]
   ))


(set! *warn-on-reflection* true)

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


(defn near-stations [lat lng offset limit]
  (-> (str METEO_API_URL "/st/near?lat=" lat "&lng=" lng "&offset=" offset "&limit=" limit)
      #_{:clj-kondo/ignore [:unresolved-var]}
      (http/get {:timeout API_TIMEOUT})
      (deref)
      (:body)
      (json/parse-string true)
      (:data)
      ))


(defn hourly-data [st-list t0 t1]
  (-> (str METEO_API_URL "/st/hourly?st=" (str/join "," st-list) "&t0=" t0 "&t1=" t1)
      #_{:clj-kondo/ignore [:unresolved-var]}
      (http/get {:timeout API_TIMEOUT})
      (deref)
      (:body)
      (json/parse-string true)
      (:series)
      ))


(def PRIORITY_LETTERS 
  #{\А \Б \В \Г \Д \Е \Ë \Ж \З \И \Й \К \Л \М \Н \О \П \Р \С \Т \У \Ф \Х \Ц \Ч \Ш \Щ \Ъ \Ы \Ь \Э \Ю \Я
    \а \б \в \г \д \е \ё \ж \з \и \й \к \л \м \н \о \п \р \с \т \у \ф \х \ц \ч \ш \щ \ъ \ы \ь \э \ю \я
    })

(defn station-title-sort [{title :title}]
  (if (PRIORITY_LETTERS (first title))
    (str "0" title)
    (str "1" title)
    ))


(defn fetch-stations []
  (let [lat 52.25 
        lng 104.3
        step 25
        pagenum 4
        ]
    (->> (range 0 (* step pagenum) step)
         (reduce
          (fn [a offset]
            (let [nst (near-stations lat lng offset step)]
              (if (< (count nst) step)
                (reduced (conj a nst))
                (conj a nst))))
          []
          )
         (mapcat identity)
         (sort-by station-title-sort)
         )))


(def STATION_FETCH_INTERVAL (* 1000 200))  ;; 200 secs

(def station-cache_ (atom {:expire 0 :data nil}))


(defn station-list []
  (let [{:keys [expire data]} @station-cache_
        now (System/currentTimeMillis)]
    (if (> expire now)
      data
      (let [data (fetch-stations)
            expire (+ now STATION_FETCH_INTERVAL)
            ]
        (reset! station-cache_ {:expire expire :data data})
        data)
      )))


;; TODO: implement cache!
;;
(defn st-hourly [st ^long hours]
  (let [t1 (ZonedDateTime/now (Clock/systemUTC))
        t0 (.minus t1 hours ChronoUnit/HOURS)
        data (hourly-data [st] (str t0) (str t1))
       ]
    (get data (keyword st))
    ))


(comment

  (st-hourly "uiii" 10)
  ;; => [{:p {:avg 960.0, :max 960, :min 960}, :t {:avg 4.0, :max 5, :min 3}, :w {:avg 2.0, :max 3, :min 1}}
  ;;     {:b {:avg 160},
  ;;      :p {:avg 960.0, :max 960, :min 960},
  ;;      :t {:avg 6.5, :max 7, :min 6},
  ;;      :w {:avg 2.0, :max 2, :min 2}}
  ;;     {:p {:avg 959.5, :max 960, :min 959}, :t {:avg 8.0, :max 8, :min 8}, :w {:avg 1.5, :max 2, :min 1}}
  ;;     {:b {:avg 110},
  ;;      :p {:avg 959.0, :max 959, :min 959},
  ;;      :t {:avg 8.5, :max 9, :min 8},
  ;;      :w {:avg 2.0, :max 2, :min 2}}
  ;;     {:p {:avg 958.0, :max 958, :min 958},
  ;;      :t {:avg 9.5, :max 10, :min 9},
  ;;      :w {:avg 1.0, :max 1, :min 1}}
  ;;     {:b {:avg 125},
  ;;      :p {:avg 957.5, :max 958, :min 957},
  ;;      :t {:avg 9.0, :max 9, :min 9},
  ;;      :w {:avg 2.0, :max 2, :min 2}}
  ;;     {:p {:avg 954.0, :max 954, :min 954}, :t {:avg 8.0, :max 8, :min 8}, :w {:avg 1.0, :max 1, :min 1}}
  ;;     {:b {:avg 25},
  ;;      :p {:avg 954.0, :max 954, :min 954},
  ;;      :t {:avg 7.0, :max 7, :min 7},
  ;;      :w {:avg 1.5, :max 2, :min 1}}
  ;;     {:b {:avg 80},
  ;;      :p {:avg 956.5, :max 958, :min 955},
  ;;      :t {:avg 6.5, :max 7, :min 6},
  ;;      :w {:avg 2.0, :max 2, :min 2}}
  ;;     {:b {:avg 180},
  ;;      :p {:avg 958.0, :max 958, :min 958},
  ;;      :t {:avg 4.0, :max 4, :min 4},
  ;;      :w {:avg 3.0, :max 3, :min 3}}]


  
  (->>
   (station-list)
   (map :title)
   )
  
  ;; => ("Абакан"
  ;;     "Аэропорт Владивосток (VVO)"
  ;;     "Аэропорт Шереметьево (SVO)"
  ;;     "Братск (А-331:218)"
  ;;     "Братск (А-331:238)"
  ;;     "Братский аэропорт"
  ;;     "Буслайка (А-331:46)"
  ;;     "Вилюй (А-331:192)"
  ;;     "Вилюй (А-331:272)"
  ;;     "Гражданпроект"
  ;;     "Гуран (А-331:23)"
  ;;     "Еловская развязка (Р-255:1858)"
  ;;     "Замзор (Р-255:1299)"
  ;;     "Иркут, обход Иркутска (Р-255:17)"
  ;;     "Иркутский аэропорт"
  ;;     "Каймоново (А-331:503)"
  ;;     "Камышет (Р-255:1326)"
  ;;     "Красноярск"
  ;;     "Куйтун (Р-255:1562)"
  ;;     "Кутулик (Р-255:1709)"
  ;;     "Мамай. Сфера"
  ;;     "Минводы"
  ;;     "Мирный"
  ;;     "Моты (Р-258:45)"
  ;;     "Николов Посад"
  ;;     "Новокузнецк"
  ;;     "Олха"
  ;;     "Олха (верх)"
  ;;     "Покосное (А-331:121)"
  ;;     "Саватеевская развязка (Р-255:1842)"
  ;;     "Солнечный"
  ;;     "Тулюшка (Р-255:1537)"
  ;;     "Тыреть (Р-255:1659)"
  ;;     "Ук (Р-255:1341)"
  ;;     "Улан-Батор"
  ;;     "Улан-Удэ, аэропорт"
  ;;     "Харик (Р-255:1579)"
  ;;     "Хомутово"
  ;;     "Хребтовая (А-331:444)"
  ;;     "Хрустальный Парк"
  ;;     "Чита"
  ;;     "Шерагул (Р-255:1516)"
  ;;     "Щеберта (Р-255:1438)"
  ;;     "Юбилейный 118"
  ;;     "Якутск"
  ;;     "Яхт-клуб Исток"
  ;;     "кафе Наш Дворик (Р-255:1605)"
  ;;     "кафе на Верблюде (Р-255:1168)"
  ;;     "кафе у Петра (Р-255:1277)"
  ;;     "обход Иркутска (Р-255:23)"
  ;;     ...)


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
