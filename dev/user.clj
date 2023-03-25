(ns user
  (:require
    [org.httpkit.server :as hk]
    [meteo38.server :as srv]
    [meteo38.config]
   ))


(comment

  (def server
    (srv/run {:host "localhost" :port 8038}))
 
  (hk/server-stop! server)
 
  (hk/server-port server)
  (hk/server-status server)
 
  ,)
