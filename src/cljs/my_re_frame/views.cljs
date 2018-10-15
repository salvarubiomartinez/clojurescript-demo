(ns my-re-frame.views
  (:require
   [re-frame.core :as re-frame]
   [my-re-frame.subs :as subs]
   ))

(defn custom-input [name]
  [:input {:type "text"
              :value @name
              :on-change #(re-frame/dispatch [:name-change (-> % .-target .-value)])
              }])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        age (re-frame/subscribe [::subs/age])]
    [:div
     [:h1 "Hello from " @name]
     [:h2 "your age: " @age] 
     [custom-input name]
     [:br]
     [:button {:on-click #(re-frame/dispatch [:age-inc])} "inc age"]
     [:div
      [:h3 "hi"]]
     ]))
