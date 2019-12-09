(ns advent-of-code-2019.day5
  (:require [advent-of-code-2019.intcode :as intcode]
            [advent-of-code-2019.util :as util]))

(def input (vec (util/numbers "day5.txt")))

(intcode/compute input [1] "day5/part1")

(intcode/compute input [5] "day5/part2")
