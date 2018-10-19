(ns my-re-frame.events
  (:require
   [re-frame.core :as re-frame]
   [my-re-frame.db :as db]
   ))

(defn turn-change [db]
  (assoc db :turn (if (= (:turn db) :X) :O :X)))

(defn play [db row col]
  (if (= " " (get-in db [:table row col]))
    (assoc-in db [:table row col] (if (= (:turn db) :X) "X" "O"))
    db))

(defn get-col [table x]
  [(get-in table [0 x]) (get-in table [1 x]) (get-in table [2 x])])

(defn get-col-table [table]
  [(get-col table 0) (get-col table 1) (get-col table 2)])

(defn one-row [row player]
  (every? (fn [x] (= x player)) row))

(defn cross-h? [table player]
  (let [values [(get-in table [0 0]) (get-in table [1 1]) (get-in table [2 2])]]
    (one-row values player)))

(defn cross-w? [table player]
  (let [values [(get-in table [0 2]) (get-in table [1 1]) (get-in table [2 0])]]
    (one-row values player)))

(defn rows? [table player]
  (reduce (fn [acc y] (if (= acc false) (one-row y player) true)) false table))

(defn get-winner [table]
  (let [col-table (get-col-table (table))]
  (cond
    (rows? (table) "X") :X
    (rows? (table) "O") :O
    (rows? col-table "X") :X
    (rows? col-table "O") :O
    (cross-h? (table) "X") :X
    (cross-h? (table) "O") :O
    (cross-w? (table) "X") :X
    (cross-w? (table) "O") :O
    :else nil)))

(defn set-winner [db]
  (assoc db :winner (get-winner (:table db))))

(defn result [table]
  (cond
    (= (get-winner table) :X) 1
    (= (get-winner table) :O) -1
    :else nil))

(defn minimax [table turn]
  (if (nil? (result table))
    (if (= turn :X)
      (max [(minimax table :O)])
      (min ((minimax table :X))))
    (result table)))

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
 :re-start
 (fn [db [_ _]]
   (assoc db :table [
           [" " " " " "]
           [" " " " " "]
           [" " " " " "]]
          :winner nil)))

(re-frame/reg-event-db
 :play
 (fn [db [_ row col]]
   (if (nil? (get-winner (:table db))) (set-winner (turn-change (play db row col))) db)))
