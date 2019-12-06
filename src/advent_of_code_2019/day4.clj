(ns advent-of-code-2019.day4)

(def input (->> (range 278384 (inc 824795)) (map str)))

;;; part 1
(defn is-pw [n]
  (let [pairs (partition 2 1 n)]
    (and (some (partial apply =) pairs)
         (apply <= (map int n)))))

(->> input (filter is-pw) count)

;;; part 2
(defn is-pw-2 [n]
  (and (apply <= (map int n))
       (some (partial = 2) (->> (frequencies n) vals))))

#_(is-pw-2 "112233")
#_(is-pw-2 "123444")
#_(is-pw-2 "111122")

(->> input (filter is-pw-2) count)
