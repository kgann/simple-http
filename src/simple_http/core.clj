(ns simple-http.core
  (:require [clojure.java.io :as io])
  (:import (java.net ServerSocket)
           (java.io BufferedReader PrintWriter BufferedOutputStream FileInputStream InputStreamReader)
           (java.util Date)))

(defn server-socket [port] (new ServerSocket port))
(defn accept [socket] (.accept socket))

(defn read-file-data [file file-lenght]
  (let [file-in (new FileInputStream file)
        file-data (byte-array file-lenght)]
    (try
      (.read file-in file-data)
      file-data
      (finally (when (some? file-in)
                 (.close file-in))))))

(defn run [connect]
  (let [in (new BufferedReader (new InputStreamReader (.getInputStream connect)))
        out (new PrintWriter (.getOutputStream connect))
        data-out (new BufferedOutputStream (.getOutputStream connect))
        input (println (.readLine in))
        file (io/file (io/resource "index.html"))
        file-lenght (int (.length file))
        file-data (read-file-data file file-lenght)]

    (.println out "HTTP/1.1 200 OK")
    (.println out "Server: Java HTTP Server from SSaurel : 1.0")
    (.println out (str "Date: " (new Date)))
    (.println out "Content-type: text/html")
    (.println out (str "Content-length: " file-lenght))
    (.println out)
    (.flush out)
    (.write data-out file-data 0 file-lenght)
    (.flush data-out)
    (.close in)
    (.close out)
    (.close data-out)
    (.close connect)))

(defn -main
  [port]
  (let [server-connect (server-socket (Integer/parseInt port))
        socket (accept server-connect)]
    (do (run socket))))
