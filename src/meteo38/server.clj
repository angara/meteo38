(ns meteo38.server
  (:require
   [org.httpkit.server :as srv]
   [ruuter.core :as ruuter]
   [meteo38.handlers :as h]
  ))


(def routes 
  [{:path     "/"
    :method   :get
    :response h/root-page
    }
   {:path     "/data"
    :method   :get
    :response h/data-block}
  ;;  {:path     "/todos"
  ;;   :method   :post
  ;;   :response add-item}
  ;;  {:path     "/todos/update/:id"
  ;;   :method   :patch
  ;;   :response update-item}
  ;;  {:path     "/todos/:id"
  ;;   :method   :patch
  ;;   :response patch-item}
  ;;  {:path     "/todos/:id"
  ;;   :method   :delete
  ;;   :response delete-item}
  ;;  {:path     "/todos"
  ;;   :method   :delete
  ;;   :response clear-completed}
   ])


(defn run [{:keys [host port] :as config}] 
  (println (format "listen at %s:%s" host port))
  ;; https://github.com/http-kit/http-kit/blob/master/src/org/httpkit/server.clj#L38
  (srv/run-server 
    #(ruuter/route routes (assoc % :config config))
    {:ip host :port port 
     :legacy-return-value? false
     :worker-name-prefix "http-kit-"
     }))
