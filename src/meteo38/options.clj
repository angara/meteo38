(ns meteo38.options
  (:require
   [meteo38.data :refer [station-list]]
  ))


(defn options-block []
  (let [opts (for [{:keys [title id]} (station-list)]
               [:option {:value id} title]
               )
        ]
    [:div#options-block.flex.justify-center 
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
      ]]
    ))
