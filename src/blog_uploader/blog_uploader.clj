(ns blog-uploader.blog-uploader
  (:require
   [next.jdbc :as j]
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

(defn post->sql [title text]
  (-> (h/insert-into :post)
       (h/columns :title :text :date)
       (h/values [[title text (java.time.LocalDateTime/now)]])
       (sql/format)))

(defn insert-post! [s]
  (transact! s))

(defn -main
  [& args]
  )
