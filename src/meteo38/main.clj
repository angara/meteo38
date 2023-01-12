(ns meteo38.main
  (:require
    [meteo38.server :as srv]
   ))


(defn -main []
  (println "meteo38.main")
  (let [host (-> (System/getenv "HTTP_SERVER_HOST") (or "localhost"))
        port (-> (System/getenv "HTTP_SERVER_PORT") (or "8038") (Integer/parseInt))]
    (srv/run {:host host :port port :backend-url "http://backend"})
    (deref (promise)
    )
  ))
