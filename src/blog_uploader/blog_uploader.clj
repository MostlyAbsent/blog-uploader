(ns blog-uploader.blog-uploader
  (:require
   [next.jdbc :as j]
   [clojure.java.io :as io]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [blog-uploader.env :refer [env]])
  (:gen-class))

(def mysql-db {:dbtype "mysql"
               :host (env :HOST)
               :dbname (env :DATABASE)
               :user (env :USERNAME)
               :password (env :PASSWORD)})

(defn transact! [q]
  (j/execute! mysql-db q))

(defn read-post [p]
  (let [f (io/file p)]
    (if (.exists f)
      {:title (->> f
                   .getName
                   (re-find #"(.*)\.md")
                   second)
       :text (slurp f)}
      (throw (Exception. "File not found.")))))

(defn post->sql [{:keys [title text]}]
  (-> (h/insert-into :post)
      (h/columns :title :text :date)
      (h/values [[title text (java.time.LocalDateTime/now)]])
      (sql/format)))

(defn -main
  [& args]
  )
