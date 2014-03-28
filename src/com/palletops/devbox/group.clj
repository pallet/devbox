(ns com.palletops.devbox.group
  "Group-spec for devbox"
  (:require
   [pallet.action :refer [with-action-options]]
   [pallet.actions :refer [exec-script exec-checked-script user]]
   [pallet.api :refer [converge node-spec group-spec plan-fn server-spec]]
   [pallet.compute :refer [instantiate-provider]]
   [pallet.core.api :refer [service-state]]
   [pallet.crate :refer [admin-user defplan]]
   [pallet.crate.automated-admin-user :refer [automated-admin-user]]
   [pallet.crate.devbox :as devbox]
   [pallet.crate.ssh-key :as ssh-key]
   [pallet.utils :refer [apply-map]]))

(defn compute-service
  "Create a service from the service map."
  [{:keys [provider] :as service}]
  (apply-map instantiate-provider provider (dissoc service :provider)))

;;; The default AMI is ubuntu 13.10
(def region-ami
  {:ap-northeast-1 "ami-27f88f26"
   :ap-southeast-1 "ami-d44e1f86"
   :ap-southeast-2 "ami-ed8b12d7"
   :eu-west-1 "ami-579a6820"
   :sa-east-1 "ami-69973474"
   :us-east-1 "ami-89181be0"
   :us-west-1 "ami-d8ac909d"
   :us-west-2 "ami-e04428d0"})

(defn default-node-spec
  "The default node spec for the given region"
  [region]
  (node-spec :image {:image-id (region-ami region)
                     :os-family :ubuntu
                     :os-version "13.10"
                     :login-user "ubuntu"}))

(defn add-user [{:keys [username keys]}]
  (user username :create-home true)
  (doseq [k keys]
    (ssh-key/authorize-key username k)))


(defplan symerc [username]
  (with-action-options {:script-prefix :no-sudo}
    (exec-script
     (if (directory? ".symerc")
       (group
        ("cd" ".symerc")
        ("git" "pull"))
       ("git" clone
        ~(str "git://github.com/" username "/.symerc"))))
    (exec-checked-script
     "Run .symerc/bootstrap"
     (if (file-exists? ".symerc/bootstrap")
       ("bash" ".symerc/bootstrap")))))


(defn devbox-spec
  "Return a devbox server spec"
  [{:keys [node-spec region github-user] :as env} users]
  (server-spec
   :phases {:bootstrap (plan-fn (automated-admin-user))
            :install (plan-fn
                         (doseq [u users]
                           (add-user u))
                       (symerc (or github-user (:username (admin-user)))))}
   :extends [(devbox/devbox-from-local
              (select-keys env [:git :lein :java]))]
   :default-phases [:install :configure]))

(defn devbox-group
  "Return a devbox group spec"
  [{:keys [node-spec region] :as env} users]
  (group-spec :devbox
    :node-spec (or node-spec (default-node-spec region))
    :extends [(devbox-spec env users)]
    :default-phases [:install :configure]))

(defn start-group
  [service spec]
  (converge (assoc spec :count 1) :compute service))

(defn stop-group
  [service spec]
  (converge (assoc spec :count 0) :compute service))

(defn nodes
  "Return all nodes with a spec."
  [service spec]
  (service-state service [spec]))
