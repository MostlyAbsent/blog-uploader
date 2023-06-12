(ns blog-uploader.blog-uploader-test
  (:require [clojure.test :refer :all]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [blog-uploader.blog-uploader :refer :all]))

(deftest execute-sql-on-db
  (testing "DB transactions can execute"
    (is (= (transact! (->
                       (h/select)
                       (h/from :post)
                       (h/columns :id)
                       (h/limit 1)
                       (sql/format)))
           [#:post{:id 4}]))))

(deftest compose-sql-insert
  (testing "Valid sql insert commands are created"
    (is (= (take 3 (post->sql "Title" "Text"))
           ["INSERT INTO post (title, text, date) VALUES (?, ?, ?)"
            "Title"
            "Text"]))))
