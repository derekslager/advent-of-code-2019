(ns advent-of-code-2019.util
  (:require [clojure.java.io :as io]))

(defn lines [resource-name]
  (->> (io/resource resource-name)
       io/reader
       line-seq))

(defn numbers [resource-name]
  (->> (io/resource resource-name)
       slurp
       (re-seq #"-?\d+")
       (map #(Integer/parseInt %))))
