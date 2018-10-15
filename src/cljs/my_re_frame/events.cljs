(ns my-re-frame.events
  (:require
   [re-frame.core :as re-frame]
   [my-re-frame.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :name-change
 (fn [db [uno value]]
   ((.-log js/console) uno)
   (assoc db :name value)))

(re-frame/reg-event-db
 :age-inc
 (fn [db [_ _]]
   (assoc db :age (inc (:age db)))))
