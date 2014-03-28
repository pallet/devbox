(ns com.palletops.devbox.github
  "Github interaction for devbox"
  (:require
   [tentacles.users :as users]))

(defn user-keys
  "Return a sequence of public key strings for the given githhub username."
  [username]
  {:pre [(string? username)]}
  (let [resp (users/user-keys username)]
    (when-let [status (:status resp)]
      (throw (ex-info (str "Could not get public key for github user" username)
                      {:type :com.palletops.devbox/github
                       :reason :invalid-use})))
    (map :key resp)))

(defn user
  "Return a user map for the given github user."
  [username]
  {:pre [(string? username)]}
  {:username username
   :keys (user-keys username)})
