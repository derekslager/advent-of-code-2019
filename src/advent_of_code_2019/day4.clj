(ns advent-of-code-2019.day4)

(def input (range 278384 (inc 824795)))

;;; part 1
(defn is-pw [n]
  (let [pairs (partition 2 1 (str n))]
    (and (some (partial apply =) pairs)
         (every? (fn [[l r]] (<= (int l) (int r))) pairs))))

(->> input (filter is-pw) count)

;;; part 2
(defn is-pw-2 [n]
  (and (is-pw n)
       (some (partial = 2) (->> (frequencies (str n)) vals))))

#_(is-pw-2 112233)
#_(is-pw-2 123444)
#_(is-pw-2 111122)

(->> input (filter is-pw-2) count)
