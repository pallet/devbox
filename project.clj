(defproject com.palletops/devbox "0.1.0-SNAPSHOT"
  :description "Pallet project for launching devboxes for pairing."
  :url "http://github.com/pallet/devbox"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "git@github.com:pallet/devbox.git"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.palletops/pallet "0.8.0-RC.9"]
                 [com.palletops/pallet-aws "0.2.1"]
                 [com.palletops/cli-cmds "0.1.0"]
                 [com.palletops/devbox-crate "0.8.0-SNAPSHOT"
                  :exclusions [commons-codec]]
                 [ch.qos.logback/logback-classic "1.0.9"]
                 [org.slf4j/jcl-over-slf4j "1.7.5"]
                 [doric "0.8.0"]
                 [tentacles "0.2.7-SNAPSHOT"
                  :exclusions
                  [commons-logging
                   com.fasterxml.jackson.core/jackson-core
                   com.fasterxml.jackson.dataformat/jackson-dataformat-smile
                   org.apache.httpcomponents/httpclient
                   org.apache.httpcomponents/httpcore
                   commons-codec]]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.1.1"]
                 [prismatic/schema "0.2.1"]]
  :plugins [[configleaf "0.4.6"]
            [lein-package "2.1.1"]
            [com.palletops/pallet-lein "0.8.0-alpha.1"]]
  :configleaf {:namespace com.palletops.devbox.project.configleaf}
  :uberjar-name "devbox.jar"
  :package {:skipjar false
            :autobuild true
            :reuse false
            :artifacts [{:build "uberjar"
                         :extension "jar" :classifier "standalone"}]}
  :hooks [configleaf.hooks
          leiningen.package.hooks.deploy
          leiningen.package.hooks.install]
  :filespecs [{:type :fn ; pull the git sha into a file for later reporting
               :fn (fn [p]
                     {:type :bytes :path "com/palletops/devbox/project/git-sha"
                      :bytes (:out (clojure.java.shell/sh
                                    "git" "rev-parse" "--verify" "HEAD"))})}])
