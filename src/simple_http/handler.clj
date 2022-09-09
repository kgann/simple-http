(ns simple-http.handler
  (:require [clojure.java.io :as io]))

(declare dispatch-request)

(defmulti handle #'dispatch-request)

(defn dispatch-request
  [[method url]]
  (some->> (dissoc (methods handle) :default)
           (filter (fn [[[m re] _v]]
                     (and (= method m)
                          (some? (re-matches re url)))))
           ffirst))

(defmethod handle :default
  [[_ _]]
  {:status 404 :body "NOT FOUND"})

(defmethod handle [:GET #"/(index.htm(l?))?"]
  [[_ _]]
  {:status 200 :body (slurp (io/resource "index.html"))})
