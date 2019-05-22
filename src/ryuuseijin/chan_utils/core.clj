(ns ryuuseijin.chan-utils.core
  (:require [clojure.core.async :refer [chan promise-chan >! <! <!! go go-loop put!]]))

(defn resolved-promise-chan
  "Returns a promise-chan that resolves to the given value."
  [v]
  (let [ch (promise-chan)]
    (put! ch v)
    ch))

(defmacro dochan
  "A shortcut for a simple go loop that executes a body for each value take from a channel."
  [[value channel] & body]
  `(go-loop [~value (<! ~channel)]
     (when (some? ~value)
       ~@body)))

(defmacro <? [& body]
  `(let [r# (<! ~@body)]
     (if (instance? Throwable r#)
       (throw r#)
       r#)))

(defmacro <!? [& body]
  `(let [r# (<!! ~@body)]
     (if (instance? Throwable r#)
       (throw r#)
       r#)))

(defmacro catchall [& body]
  `(try
     ~@body
     (catch Throwable e#
       e#)))

(defmacro go-catchall [& body]
  `(go (catchall ~@body)))
