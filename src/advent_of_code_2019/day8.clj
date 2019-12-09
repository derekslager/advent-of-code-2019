(ns advent-of-code-2019.day8
  (:require [advent-of-code-2019.util :as util]
            [clojure.java.io :as io]))

(def input (-> (io/resource "day8.txt") slurp))

(def layers (partition (* 25 6) input))

;;; part 1

(let [layer (->> layers
                 (map frequencies)
                 (apply min-key #(get % \0)))]
  (* (get layer \1) (get layer \2)))

;;; part 2

(defn stack [plo phi]
  (case phi
    \2 plo
    phi))

(->> layers
     (reverse)
     (reduce
      (fn [image rows]
        (map stack image rows)))
     (partition 25))
