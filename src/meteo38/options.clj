(ns meteo38.options
  (:require
   [hiccup2.core :refer [html raw]]
   [meteo38.data :refer [station-list]]
   [meteo38.icons :as ico]
  ))

    
;; NOTE: tailwind classes
;; bg-success bg-success-600 bg-success-700
;; bg-danger bg-danger-600 bg-danger-700
(defn button [action text bg]
        [:button {:type "button"
                  :class (str "inline-block rounded px-3 font-medium bg-" bg " "
                          "text-white shadow-[0_4px_9px_-4px_#14a44d] "
                          "hover:bg-" bg "-600 hover:shadow-[0_8px_9px_-4px_rgba(20,164,77,0.3),0_4px_18px_0_rgba(20,164,77,0.2)] "
                          "focus:bg-" bg "-600 focus:shadow-[0_8px_9px_-4px_rgba(20,164,77,0.3),0_4px_18px_0_rgba(20,164,77,0.2)] focus:outline-none focus:ring-0 "
                          "active:bg-" bg "-700 active:shadow-[0_8px_9px_-4px_rgba(20,164,77,0.3),0_4px_18px_0_rgba(20,164,77,0.2)] "
                          "transition duration-150 ease-in-out")
                  :onclick (str "stlist_update('" action "')")}
         (raw text)]
  )

(defn options-block []
  (let [opts (for [{:keys [st title]} (station-list)]
               [:option {:value st} title])
        ]
    (html
     [:div.flex.mx-1.my-1.gap-2 
      [:div.grow.flex.items.center.my-1 {:class "xl:w-96"} 
       [:select#sel_stations 
        {:class (str "form-select appearance-none block w-full "
                  "shadow-[0_4px_9px_-4px_rgba(20,20,20,0.3)] "
                  "hover:shadow-[0_8px_9px_-4px_rgba(20,20,20,0.3),0_4px_18px_0_rgba(20,20,20,0.2)] "
                  "px-2 py-1 text-base font-normal text-gray-700 "
                  "bg-white bg-clip-padding bg-no-repeat "
                  "border border-solid border-gray-300 rounded transition ease-in-out m-0 "
                  "focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none")
         }
        opts
        ]
       ]
      
      [:div.flex.py-1.gap-2
       (button "top"    ico/arrow-up-short-svg "success") 
       (button "bottom" ico/arrow-down-short-svg "success")
       (button "remove" ico/x-svg "danger")
       ]
      ])
    ))
