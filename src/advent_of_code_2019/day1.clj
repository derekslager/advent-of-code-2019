(ns advent-of-code-2019.day1
  (:require [advent-of-code-2019.util :as util]
            [clojure.string :as str]))

;;; part 1
(defn fuel-required [mass]
  (-> mass
      (/ 3)
      Math/floor
      (- 2)
      int))

#_(fuel-required 12)
#_(fuel-required 14)
#_(fuel-required 1969)

(->> (util/numbers "day1.txt")
     (map fuel-required)
     (reduce +))

;;; part 2
(defn fuel-required-full [mass]
  (->> mass
       (iterate fuel-required)
       (drop 1)
       (take-while pos?)
       (reduce +)))

#_(fuel-required-full 14)
#_(fuel-required-full 1969)

(->> (util/numbers "day1.txt")
     (map fuel-required-full)
     (reduce +))
