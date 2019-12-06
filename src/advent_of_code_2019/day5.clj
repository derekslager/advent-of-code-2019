(ns advent-of-code-2019.day5
  (:require [clojure.string :as str]
            [advent-of-code-2019.util :as util]))

(defn compute [input input-value]
  (loop [pos 0, state input, output nil]
    (let [code (nth state pos)
          [_ pmodes op-code] (re-matches #"(\d*)(\d{2})" (str code))
          op (if op-code (Integer/parseInt op-code) code)
          modes (if-not (str/blank? pmodes)
                  (map (fn [p]
                         (condp = p
                           \0 #(get state %)
                           \1 (fn [x] x)))
                       (reverse (format "%03d" (Integer/parseInt pmodes))))
                  (repeat #(get state %)))
          praw (fn [index] (get state (+ 1 index pos)))
          pval (fn [index] ((nth modes index) (praw index)))]
      (condp contains? op
        #{99} output
        #{1 2}
        (recur (+ pos 4)
               (assoc state (praw 2) ((case op 1 + 2 *) (pval 0) (pval 1)))
               output)
        #{3}
        (recur (+ pos 2)
               (assoc state (praw 2) input-value)
               output)
        #{4}
        (if (and (not (nil? output)) (not= 0 output))
          :error
          (recur (+ pos 2)
                 state
                 (pval 0)))
        #{5 6}
        (recur (if ((case op 5 #(not= 0 %) 6 #(= 0 %)) (pval 0))
                 (pval 1)
                 (+ pos 3))
               state
               output)
        #{7}
        (recur (+ pos 4)
               (assoc state (praw 2) (if (< (pval 0) (pval 1)) 1 0))
               output)
        #{8}
        (recur (+ pos 4)
               (assoc state (praw 2) (if (= (pval 0) (pval 1)) 1 0))
               output)))))

(def input (vec (util/numbers "day5.txt")))

(compute input 1)
(compute input 5)
