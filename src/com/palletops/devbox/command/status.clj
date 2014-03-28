(ns com.palletops.devbox.command.status
  "Status of a devbox"
  (:require
   [clojure.string :refer [join]]
   [com.palletops.cli.command :refer [def-command-fn]]
   [com.palletops.devbox.api :as api]
   [com.palletops.devbox.command-utils :refer [print-target-nodes]]))

(def-command-fn status
  "Status a devbox."
  [["github-users" "Github usernames to authorize on the devbox" :vararg true]]
  []
  [{:keys [] :as options} [& github-users]]
  (assert (every? string? github-users))
  (print-target-nodes (api/status)))
