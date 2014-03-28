{:dev {:dependencies [[com.palletops/pallet "0.8.0-RC.9" :classifier "tests"
                       :exclusions [commons-codec]]]
       :plugins [[com.palletops/pallet-lein "0.8.0-alpha.1"]]
       :leiningen/reply
       {:dependencies [[org.slf4j/jcl-over-slf4j "1.7.2"]]
        :exclusions [commons-logging]}
       :checkout-deps-shares ^:replace [:source-paths :test-paths
                                        :compile-path]}}
