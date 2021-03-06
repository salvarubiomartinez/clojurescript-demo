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
        age (re-frame/subscribe [::subs/age])
        winner (re-frame/subscribe [::subs/winner])
        table (re-frame/subscribe [::subs/table])]
    [:div
     [:table
      [:tbody
       (doall (for [i (range 3)]
         ^{:key (str "tr-" i)}
         [:tr (doall (for [j (range 3)]
         ^{:key (str "td-" j)}
                [:td
                 {:on-click #(re-frame/dispatch [:play i j])
                      :style {:border "1px solid black" :width "30px" :height "30px" :align "center"}}
                 (get-in @table [i j])]))]))]]

     [:br]
     [:button {:on-click #(re-frame/dispatch [:re-start])} "restart"]
     [:h3 "the winner is " @winner]
     ]))
