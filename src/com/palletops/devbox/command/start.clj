(ns com.palletops.devbox.command.start
  "Start a devbox"
  (:require
   [clojure.string :refer [join]]
   [com.palletops.cli.command :refer [def-command-fn]]
   [com.palletops.devbox.api :as api]
   [com.palletops.devbox.command-utils :refer [report-session]]))

(def-command-fn start
  "Start a devbox."
  [["github-users" "Github usernames to authorize on the devbox" :vararg true]]
  []
  [{:keys [] :as options} [& github-users]]
  (println "Start a devbox with github users:" (join ", " github-users))
  (assert (every? string? github-users))
  (report-session (api/start github-users)))
