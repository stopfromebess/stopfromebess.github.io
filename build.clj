(require '[babashka.fs :as fs])
(require '[babashka.http-server :as http-server])
(require '[selmer.parser])
(require '[clojure.java.io :as io])

(def out-dir "docs")
(def files (map #(.toString %)
                (fs/glob "." "*.html")))

(when-not (fs/directory? out-dir)
    (fs/create-dir out-dir))

(defn clean!
  []
  (dorun (map (fn [f] (io/delete-file (io/as-file (str out-dir "/" f))
                                      true)) files)))

(defn render!
  []
  (dorun (map (fn [f] (spit (str out-dir "/" f)
                            (selmer.parser/render-file (str "./" f) {})))
              files)))

(clean!)
(render!)
(http-server/exec {:dir "docs" :port 8080})