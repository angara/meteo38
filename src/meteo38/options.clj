(ns meteo38.options)


(defn options-block [_req]

[:div.flex.justify-center 
 [:div {:class "mb-3 xl:w-96"} 
  [:select {:class "form-select appearance-none 
                    block
      w-full
      px-3
      py-1.5
      text-base
      font-normal
      text-gray-700
      bg-white bg-clip-padding bg-no-repeat
      border border-solid border-gray-300
      rounded
      transition
      ease-in-out
      m-0
      focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"}
   [:option {:selected 1} "item 1"]
   [:option {:selected 1} "item 2"]
   [:option {:selected 1} "item 3"]
   [:option {:selected 1} "item 4"]
  ]
   ]]
  )
