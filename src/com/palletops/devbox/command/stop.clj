(ns com.palletops.devbox.command.stop
  "Stop a devbox"
  (:require
   [com.palletops.cli.command :refer [def-command-fn]]
   [com.palletops.devbox.api :as api]
   [com.palletops.devbox.command-utils :refer [report-session]]))

(def-command-fn stop
  "Stop a devbox."
  []
  []
  [{:keys [] :as options} [& github-users]]
  (println "Stop a devbox with github users " (pr-str github-users))
  (assert (every? string? github-users))
  (report-session (api/stop)))
