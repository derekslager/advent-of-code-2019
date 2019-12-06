(ns advent-of-code-2019.day6
  (:require [advent-of-code-2019.util :as util]
            [ubergraph.alg :as ga]
            [ubergraph.core :as g]))

(def input (util/lines "day6.txt"))

(def parsed (for [pair input]
              (let [[_ l r] (re-matches #"(\w+)\)(\w+)" pair)]
                [l r])))
(def rmap (->> parsed (group-by second)))

(defn count-orbits [map k]
  (if-let [orbits (get map k)]
    (->> (for [[orbit] orbits :when (not= orbit k)]
           (+ 1 (count-orbits map orbit)))
         (reduce +))
    0))

;;; part 1

(->> (keys rmap)
     (map (partial count-orbits rmap))
     (reduce +))

;;; part 2

(let [graph (apply g/graph parsed)]
  (-> (ga/shortest-path graph "YOU" "SAN") :cost (- 2)))
