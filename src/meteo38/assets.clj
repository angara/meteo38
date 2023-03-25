(ns meteo38.assets
  (:require
    [clojure.string :as str]
    [babashka.fs :as fs]
    [meteo38.config :refer [ASSETS_DIR]]
    [meteo38.util :refer [instant->rfc1123]]
   ))


;; https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
(def EXT_CTYPE_MAP 
  {"css" "text/css"
   "ico" "image/vnd.microsoft.icon"
   "js"  "text/javascript"
   "jpg" "image/jpeg"
   "png" "image/png"
   "svg" "image/svg+xml"
   })


(defn name->path-ctype [f]
  (let [fname   (fs/file-name f)
        [_ ext] (fs/split-ext fname)
        ]
    ;; [fname [path ctype]]
    [fname [f (get EXT_CTYPE_MAP ext "application/octet-stream")]]
    ))


(defn load-assets-map [path]
  (->> path
      (fs/list-dir)
      (map name->path-ctype) 
      (into {}) 
      ))


(def ASSETS_MAP 
  (load-assets-map ASSETS_DIR))


(defn static-assets-handler [fname]
  (if-let [[path ctype] (get ASSETS_MAP fname)]
    (let [file (fs/file path)]
      {:status 200
       :body file
       :headers { "Content-Type" ctype
                  "Last-Modified" (-> file
                                    (fs/last-modified-time)
                                    (fs/file-time->instant)
                                    (instant->rfc1123))
                  "Cache-control" "public, max-age=604800" ;; , immutable
        }
       })
    {:status 404 :body "asset not found"}
    ))
