(ns com.palletops.devbox.command.aws
 "Set up AWS credentials"
 (:require
  [com.palletops.cli.command :refer [def-command-fn]]
  [com.palletops.devbox.api :as api]))

(def-command-fn aws
  "Set up AWS credentials for devbox."
  [["AWS-Key" "AWS API key"]
   ["AWS-Secret" "AWS API secret"]]
  []
  [{:keys [] :as options} [id credential]]
  (let [service {:provider :pallet-ec2
                 :identity id
                 :credential credential}]
    (api/service service)))
