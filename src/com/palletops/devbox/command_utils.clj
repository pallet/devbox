(ns com.palletops.devbox.command-utils
  "Utils for CLI commands"
  (:require
   [clojure.pprint :refer [pprint]]
   [doric.core :refer [table]]
   [pallet.core.api :refer [phase-errors throw-phase-errors]]
   [pallet.node :refer [hardware node-map]]
   [pallet.repl :refer [explain-session]]))

(defn print-target-nodes
  [targets]
  (println
   (table
    [:group-name :primary-ip :ram :cpus :size]
    (map
     #(let [hw (hardware (:node %))]
        (merge %
               (node-map (:node %))
               {:cpus (count (:cpus hw))
                :size (:id hw)
                :ram (:ram hw)}))
     targets))))

(defn report-session
  "Report a session result"
  [s]
  (when (seq (phase-errors s))
    (clojure.pprint/pprint (vec (phase-errors s)))
    (explain-session s))
  (throw-phase-errors s)
  (print-target-nodes (:targets s)))
