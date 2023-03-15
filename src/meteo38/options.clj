(ns meteo38.options
  (:require
   [hiccup2.core :refer [html raw]]
   [meteo38.data :refer [station-list]]
  ))


(defn options-block []
  (let [opts (for [{:keys [title id]} (station-list)]
               [:option {:value id} title])
        ]
    (html
     [:div#options-block.flex.mx-2.gap-2 
      [:div {:class "xl:w-96"} 
       [:select {:class "form-select appearance-none block w-full
         px-3 py-1.5 text-base font-normal text-gray-700
         bg-white bg-clip-padding bg-no-repeat
         border border-solid border-gray-300 rounded transition ease-in-out m-0
         focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                 :onchange "options_onchange(this)" 
                 :onfocus "options_onchange(this)"
                 }
        opts
        ]
       ]
      [:button {; :hx-get "/options" :hx-trigger "click" :hx-target "#options-block" :hx-swap "outerHTML"
                :type "button" 
                :class "inline-block rounded-full px-4 border-2 border-primary
                        font-medium text-lg
                        text-primary
                        hover:border-primary-600 hover:bg-neutral-500 hover:bg-opacity-10 hover:text-primary-600 
                        focus:border-primary-600 focus:text-primary-600 focus:outline-none focus:ring-0 
                        active:border-primary-700 active:text-primary-700
                        transition duration-150 ease-in-out"
                :onclick "options_update(this)"
                ;; leading-tight
                }
       (raw "&plus;")
       ]
      ]
      )
    ))


                ;; :class "inline-block px-4 bg-blue-600 text-white font-medium text-base rounded-full shadow-md 
                ;;         hover:bg-blue-700 hover:shadow-lg 
                ;;         focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 
                ;;         active:bg-blue-800 active:shadow-lg 
                ;;         transition duration-150 ease-in-out"
