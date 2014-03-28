(ns com.palletops.devbox.env
  "A configuration environment for devbox"
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :refer [file reader]]
   [clojure.pprint :refer [pprint]]
   [schema.core :as schema])
  (:import
   java.io.File
   [clojure.lang Keyword LineNumberingPushbackReader]))

(def Env
  {:service {:provider schema/Keyword schema/Keyword schema/Any}
   (schema/optional-key :github-user) schema/Str
   (schema/optional-key :region) Keyword
   (schema/optional-key :node-spec) {schema/Keyword schema/Any}
   (schema/optional-key :admins) [{:username schema/Str :key schema/Str}]
   (schema/optional-key :lein) {schema/Keyword schema/Any}})

(defn verify
  "Verify an environment map."
  [m]
  (schema/validate Env m))

(def ^:dynamic
  *env-file*
  "A binding to allow override of the env file location"
  nil)

(def default-env
  {:region :us-east-1})

(defn env-file
  "File for environment maps."
  []
  (or (if-let [path (System/getenv "DEVBOX_ENV")] (file path))
      *env-file*
      (file (System/getProperty "user.home") ".devbox" "environment.edn")))

(defn load-environments
  "Load environment maps."
  [f]
  (when (.canRead f)
    (with-open [rdr (LineNumberingPushbackReader. (reader f))]
      (try
        (edn/read rdr)
        (catch Exception e
          (throw
           (ex-info (str "Could not read " (str f)
                         ":" (.getLineNumber rdr)
                         ":" (.getColumnNumber rdr)
                         ".  " (.getMessage e))
                    {:type :com.palletops.devbox/environment
                     :reason :invalid-environment-file
                     :file (str f)
                     :line (.getLineNumber rdr)
                     :column (.getColumnNumber rdr)
                     :exit-code 1}
                    e)))))))

(defn write-environments
  "Write environment maps."
  [^File f environments]
  (.mkdirs (.getParentFile f))
  (spit f (with-out-str (pprint environments))))

(defn environments
  "Load the environments, creating a default environment if one
  doesn't already exist."
  [f]
  (if-let [environments (load-environments f)]
    environments
    (let [environments {:default default-env}]
      (write-environments f environments)
      environments)))

(defn env
  "Lookup an environment."
  [kw]
  (let [environments (environments (env-file))]
    (when-not environments
      )
    (let [env-map (get environments kw)]
      (when-not env-map
        (throw
         (ex-info (str "Failed to load environment " kw " from: " (env-file))
                  {:type :com.palletops.devbox/environment-failed-to-load
                   :kw kw
                   :file (str (env-file))})))
      (verify env-map))))
