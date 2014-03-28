(ns com.palletops.devbox
  "A Command Line for launching devboxes for pairing."
  (:require
   [clojure.tools.logging :refer [error]]
   [com.palletops.cli.api :refer [report-exceptions]]
   [com.palletops.cli.command :refer [def-command]]
   [com.palletops.cli.context :refer [initial-context]]))

(def-command ^:cli/main cli-main "devbox command line tool." [])

(def cli-config
  {:project-name "Debox Pairing Boxes"
   :self-name "devbox"
   :project-ns 'com.palletops.devbox.project.configleaf
   :sha-resource "com/palletops/devbox/project/git-sha"
   :ns-prefixes ["com.palletops.devbox.command."
                 "com.palletops.cli.command.server"]
   :commands ['com.palletops.cli.command.help
              'com.palletops.cli.command.version]
   :static-help-path "devbox/help"
   :main-var #'cli-main})

(defn -main [& args]
  (report-exceptions
   (try
     (cli-main (initial-context cli-config)
               (if (not= args [""]) args))
     (catch Exception e
       (error e "devbox failed")
       (let [{:keys [type] :as data} (ex-data e)]
         (if type
           (throw (ex-info (.getMessage e) (assoc data :exit-code 1) e))
           (throw e)))))))
