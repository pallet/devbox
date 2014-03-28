(ns com.palletops.devbox.api
  "Main devbox API"
  (:require
   [com.palletops.devbox.env :as env]
   [com.palletops.devbox.github :as github]
   [com.palletops.devbox.group :as group]))

(defn service
  "Specify a service to use."
  [{:keys [id credential provider] :as service}]
  (let [f (env/env-file)]
    (env/write-environments
     f (assoc-in (env/environments f) [:default :service] service))))

(defn start
  "Start a devbox.  Returns a pallet session."
  [github-users]
  (let [{:keys [service] :as env} (env/env :default)
        users (mapv github/user github-users)
        service (group/compute-service service)
        spec (group/devbox-group env users)]
    (group/start-group service spec)))

(defn stop
  "Stop a devbox.  Returns a pallet session."
  []
  (let [{:keys [service] :as env} (env/env :default)
        service (group/compute-service service)
        spec (group/devbox-group env [])]
    (group/stop-group service spec)))

(defn status
  "Status of a devbox.  Returns a sequence of pallet nodes."
  []
  (let [{:keys [service] :as env} (env/env :default)
        service (group/compute-service service)
        spec (group/devbox-group env [])]
    (group/nodes service spec)))
