(ns advent-of-code-2019.day5
  (:require [clojure.string :as str]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [advent-of-code-2019.util :as util]))

(defn compute-async [ops input-ch result-ch & [id]]
  (let [output-ch (async/chan)]
    (async/go-loop [{:keys [pos state output] :as ctx} {:pos 0, :state ops}]
      (let [code (nth state pos)
            [_ pmodes op-code] (re-matches #"(\d*)(\d{2})" (str code))
            op (if op-code (Integer/parseInt op-code) code)
            modes (if-not (str/blank? pmodes)
                    (map (fn [p]
                           (condp = p
                             \0 #(get state %)
                             \1 (fn [x] x)))
                         (reverse (format "%03d" (Integer/parseInt pmodes))))
                    (repeat #(get state %)))
            praw (fn [index] (get state (+ 1 index pos)))
            pval (fn [index] ((nth modes index) (praw index)))]
        (condp contains? op
          #{99}
          (do
            (log/info id "halted!" output)
            (async/>! result-ch output)
            (async/close! result-ch))
          #{1 2}
          (recur (-> (update ctx :pos + 4)
                     (update :state assoc (praw 2) ((case op 1 + 2 *) (pval 0) (pval 1)))))
          #{3}
          (do
            (log/info id "reading next input value")
            (let [next-input-value (async/<! input-ch)]
              (log/info id "read:" next-input-value)
              (recur (-> (update ctx :pos + 2)
                         (update :state assoc (praw 0) next-input-value)))))
          #{4}
          (let [output (pval 0)]
            (log/info id "put:" output)
            (async/put! output-ch output)
            (recur (-> (update ctx :pos + 2)
                       (assoc :output output))))
          #{5 6}
          (recur (assoc ctx :pos
                        (if ((case op 5 #(not= 0 %) 6 #(= 0 %)) (pval 0))
                          (pval 1)
                          (+ pos 3))))
          #{7}
          (recur (-> (update ctx :pos + 4)
                     (update :state assoc (praw 2) (if (< (pval 0) (pval 1)) 1 0))))
          #{8}
          (recur (-> (update ctx :pos + 4)
                     (update :state assoc (praw 2) (if (= (pval 0) (pval 1)) 1 0)))))))
    output-ch))

(defn fixed-inputs [inputs]
  (let [ch (async/chan (async/buffer (count inputs)))]
    (doseq [input inputs]
      (async/>!! ch input))
    ch))

(defn compute [ops inputs & [id]]
  (let [result-ch (async/chan)]
    (compute-async ops (fixed-inputs inputs) result-ch id)
    (async/<!! result-ch)))

(def input (vec (util/numbers "day5.txt")))

(compute input [1] "day5/part1")

(compute input [5] "day5/part2")
