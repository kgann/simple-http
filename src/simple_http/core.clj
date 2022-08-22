(ns simple-http.core
  (:require [clojure.java.io :as io])
  (:import [java.net ServerSocket Socket])
  (:gen-class))

(defn headers
  [^String data]
  ["HTTP/1.1 200 OK"
   "Server: kgann/1.0 Clojure"
   (str "Date: " (java.util.Date.))
   "Content-type: text/html"
   (str "Content-length: " (count (.getBytes data)))])

(defn write
  [^Socket socket data]
  (with-open [out (io/writer socket)]
    (doseq [header (headers data)]
      (.write out header)
      (.write out "\n"))
    ;; blank line between headers and content
    (.write out "\n")
    (.write out data)))

(defn handle
  [^Socket socket]
  (write socket (slurp (io/resource "index.html"))))

(defn -main
  [& _args]
  (let [server (ServerSocket. 8080)]
    (while true
      (let [connection (.accept server)]
        (.start (Thread. #(handle connection)))))))
