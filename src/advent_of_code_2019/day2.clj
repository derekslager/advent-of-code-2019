(ns advent-of-code-2019.day2
  (:require [advent-of-code-2019.util :as util]
            [clojure.string :as str]))

;;; part 1
(defn compute [input]
  (loop [pos 0, state input]
    (let [op (nth state pos)]
      (if (= op 99)
        state
        (let [[i1 i2 o] (subvec state (inc pos))
              v1 (get state i1)
              v2 (get state i2)]
          (recur (+ pos 4)
                 (assoc state o ((case op 1 + *) v1 v2))))))))

#_(compute [1 0 0 0 99])

(-> (util/numbers "day2.txt")
    vec
    (assoc 1 12)
    (assoc 2 2)
    compute
    (nth 0))

;;; part 2
(defn attempts [state]
  (for [noun (range 0 100), verb (range 0 100)]
    (let [state' (-> state (assoc 1 noun) (assoc 2 verb))]
      [(nth (compute state') 0) noun verb])))

(let [state (vec (util/numbers "day2.txt"))]
  (let [[_ noun verb]
          (->> (attempts state)
               (filter (fn [[v & _]] (= v 19690720)))
               (first))]
      (+ (* 100 noun) verb)))
