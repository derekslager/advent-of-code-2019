(ns advent-of-code-2019.day7
  (:require [advent-of-code-2019.intcode :as intcode]
            [clojure.math.combinatorics :as combo]
            [advent-of-code-2019.util :as util]
            [clojure.core.async :as async]))

(defn phase-settings [start end] (combo/permutations (range start end)))

(def input (vec (util/numbers "day7.txt")))

;;; part 1

(->> (for [settings (phase-settings 0 5)]
       (reduce
        (fn [last-output phase-setting]
          (intcode/compute input [phase-setting last-output] "day7/part1"))
        0
        settings))
     (apply max))

;;; part 2

(->> (for [settings (phase-settings 5 10)]
       (let [input-chs (vec (repeatedly 5 #(async/chan 10)))
             result-chs (vec (repeatedly 5 #(async/chan 10)))]

         (doseq [[ch phase-setting] (map vector input-chs settings)]
           (async/put! ch phase-setting))

         (let [first-ch (nth input-chs 0)]
           (async/>!! first-ch 0)
           (doseq [index (range 5)]
             (async/pipe
              (intcode/compute-async input (nth input-chs index) (nth result-chs index) (str "day7/amp" index))
              (nth input-chs (mod (inc index) 5))))

           (async/<!! (nth result-chs 4)))))
     (apply max))
