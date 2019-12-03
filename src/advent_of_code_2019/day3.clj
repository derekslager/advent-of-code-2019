(ns advent-of-code-2019.day3
  (:require
    [clojure.string :as str]
    [advent-of-code-2019.util :as util]
    [clojure.set :as set]))

(defn manhattan [[x1 y1] [x2 y2]]
  (+ (Math/abs (- x1 x2)) (Math/abs (- y1 y2))))

(defn points [start instruction]
  (let [[index step]
        (condp = (nth instruction 0)
          \U [1 1] \D [1 -1] \R [0 1] \L [0 -1])
        count (Integer/parseInt (subs instruction 1))]
    (->> (range (+ step (nth start index))
                (+ (nth start index) (* step (inc count)))
                step)
         (map (fn [i] (assoc start index i))))))

#_(points [8 5] "L5")
#_(points [0 0] "R3")

(defn draw-line [wire-string]
  (->> (str/split wire-string #",")
       (reduce
        (fn [s instruction]
          (concat s (points (last s) instruction)))
        [[0 0]])))

;;; part 1
(let [[w1 w2] (util/lines "day3.txt")
      l1 (draw-line w1)
      l2 (draw-line w2)
      intersections (-> (set/intersection (into #{} l1) (into #{} l2)) (disj [0 0]))]
  (->> intersections
       (map (fn [point]
              [point (manhattan point [0 0])]))
       (sort-by second)
       (first)))

;;; part 2
(defn index-of [needle haystack]
  (->> (keep-indexed #(when (= %2 needle) %1) haystack)
       first))

(let [[w1 w2] (util/lines "day3.txt")
      l1 (draw-line w1)
      l2 (draw-line w2)
      intersections (-> (set/intersection (into #{} l1) (into #{} l2)) (disj [0 0]))]
  (sort (for [intersection intersections]
          (+ (index-of intersection l1)
             (index-of intersection l2)))))
