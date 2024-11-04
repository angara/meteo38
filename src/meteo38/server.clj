(ns meteo38.server
  (:require
   [clojure.string :as str] 
   [org.httpkit.server :as srv]
   [meteo38.handlers :as h]
   [meteo38.exp-tjs :refer [tjs]]
   [meteo38.assets :refer [static-assets-handler]]
   [meteo38.util :refer [wrap-query-params]]
   ))


(defn handler [req]
  ;; don't care about http methods 
  (let [[_ root fname] (-> req :uri (str/split #"/" 3))
        hdl          (case root
                       ""        h/root-page
                       "data"    h/data-page
                       "map"     h/geo-map 
                       "map-data"  h/geo-map-data
                       "options" h/options
                       "svgraph" h/svgraph
                       "assets"  (fn [_] (static-assets-handler fname))
                       "exp"     (when (= "t.js" fname) tjs)
                       "ext"     (when (= "t.js" fname) tjs)
                       nil)
        ]
    (if hdl
      (hdl req)
      {:status 404 :body "Not Found"})  
    ))


(defn run [{:keys [host port]}] 
  (println (format "listen at %s:%s" host port))
  ;; https://github.com/http-kit/http-kit/blob/master/src/org/httpkit/server.clj#L38
  (srv/run-server 
    (wrap-query-params #'handler)
    {:ip host 
     :port port 
     :legacy-return-value? false
     :worker-name-prefix "http-kit-"
     }))
