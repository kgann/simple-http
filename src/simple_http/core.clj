(ns simple-http.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [simple-http.handler :as handler])
  (:import [java.net ServerSocket Socket])
  (:gen-class))

(def ^:private ->status-str
  {200 "200 OK"
   404 "404 NOT FOUND"})

(defn ^:private headers
  [status ^String data]
  [(str "HTTP/1.1 " (->status-str status))
   "Server: kgann/1.0 Clojure"
   (str "Date: " (java.util.Date.))
   "Content-type: text/html"
   (str "Content-length: " (count (.getBytes data)))])

(defn ^:private write
  [^Socket socket status body]
  (with-open [out (io/writer socket)]
    (doseq [header (headers status body)]
      (.write out header)
      (.write out "\n"))
    ;; blank line between headers and content
    (.write out "\n")
    (.write out body)))

(defn ^:private read-request
  [socket]
  (let [in (io/reader socket)]
    (as-> (line-seq in) $
      (first $)
      (string/split $ #"\s+")
      (take 2 $)
      (vec $)
      (update $ 0 keyword))))

(defn ^:private respond
  [socket]
  (let [[method url] (read-request socket)
        {:keys [body status]} (handler/handle [method url])]
    (write socket status body)))

(defn -main
  [& [port]]
  (let [server (ServerSocket. (Integer/parseInt (or port "8080")))]
    (while true
      (let [connection (.accept server)]
        (.start (Thread. #(respond connection)))))))
