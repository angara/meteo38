(ns meteo38.main
  (:require
    [meteo38.config :as config]
    [meteo38.server :as srv]
   ))


(defn -main []
  (println "meteo38.main")
  (srv/run {:host config/HTTP_SERVER_HOST 
            :port config/HTTP_SERVER_PORT 
           })
  (deref (promise)
  ))
