(ns blog-uploader.blog-uploader-test
  (:require [clojure.test :refer :all]
            [honey.sql :as sql]
            [clojure.java.io :as io]
            [honey.sql.helpers :as h]
            [test-with-files.tools :refer [with-files]]
            [blog-uploader.blog-uploader :refer :all]))

(deftest read-post-from-file
  (testing "A markdown file is correctly read into the program"
    (is (= (with-files tmp-dir ["test-1.md" "test"]
             (read-post (str tmp-dir "/test-1.md")))
           {:title "test-1"
            :text "test"}))
    (is (thrown? Exception (read-post "notapath")))))

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
