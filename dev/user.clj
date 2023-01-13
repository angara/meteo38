(ns user
  (:require
    [org.httpkit.server :as hk]
    [meteo38.server :as srv]
   ))


(comment

  (def server
    (srv/run {:host "localhost" :port 8038 :backend-url "http://backend"}))
  
  (hk/server-stop! server)
  
  (hk/server-port server)
  (hk/server-status server)
  
  ,)
