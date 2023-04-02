(ns meteo38.icons
  (:require
   [meteo38.config :refer [ASSETS_DIR]]
   ))


(def arrow-down-short-svg   (slurp (str ASSETS_DIR "/arrow-down-short.svg")))
(def arrow-up-short-svg     (slurp (str ASSETS_DIR "/arrow-up-short.svg")))
(def x-svg                  (slurp (str ASSETS_DIR "/x.svg")))

(def external-link-svg      (slurp (str ASSETS_DIR "/external-link.svg")))

;; (def arrow-down-square-svg  (slurp (str ASSETS_DIR "/arrow-down-square.svg")))
;; (def arrow-up-square-svg    (slurp (str ASSETS_DIR "/arrow-up-square.svg")))
;; (def x-square-svg           (slurp (str ASSETS_DIR "/x-square.svg")))
