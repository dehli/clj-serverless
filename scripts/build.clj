(require '[cljs.build.api :as b])

(defonce build-cmd
  (if (= (last *command-line-args*) "--watch")
    b/watch b/build))

(build-cmd "src"
           {:main 'serverless.core
            :output-dir "out/"
            :output-to "build/js/main.js"
            :optimizations :simple
            :target :nodejs})
