(ns meteo38.server
  (:require
   [org.httpkit.server :as srv]
   [ruuter.core :as ruuter]
   [meteo38.handlers :as h]
   [meteo38.assets :refer [static-assets-handler]]
   [meteo38.util :refer [wrap-query-params]]
   ))


(def routes 
  [{:path     "/"
    :method   :get
    :response (wrap-query-params h/root-page)}
   
   {:path     "/data"
    :method   :get
    :response (wrap-query-params h/data-page)}
   ;
   {:path     "/assets/:fname"
    :method   :get
    :response static-assets-handler}
  
   ])


(defn run [{:keys [host port] :as config}] 
  (println (format "listen at %s:%s" host port))
  ;; https://github.com/http-kit/http-kit/blob/master/src/org/httpkit/server.clj#L38
  (srv/run-server 
    #(ruuter/route routes (assoc % :config config))
    {:ip host 
     :port port 
     :legacy-return-value? false
     :worker-name-prefix "http-kit-"
     }))
