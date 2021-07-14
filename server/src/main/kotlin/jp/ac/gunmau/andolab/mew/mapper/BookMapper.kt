package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Book
import jp.ac.gunmau.andolab.mew.model.BookWithRate
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface BookMapper {
    @Insert("INSERT INTO book(user_id, title, public) VALUES(#{userId}, #{title}, #{public})")
    fun insert(model: Book): Int

    @Select("SELECT * FROM book WHERE book_id = #{id}")
    fun selectWithId(id: Int): Book?

    @Select("SELECT * FROM book WHERE user_id = #{userId}")
    fun selectWithUserId(userId: Int): List<Book>

    @Select("SELECT * FROM book WHERE user_id = #{userId} AND public = true")
    fun selectPublicWithUserId(userId: Int): List<Book>

    @Select("SELECT * FROM book WHERE title LIKE #{pattern} LIMIT 300")
    fun findByTitle(pattern: String): List<Book>

    @Select("SELECT * FROM book WHERE title LIKE #{pattern} AND public = true LIMIT 300")
    fun findPublicByTitle(pattern: String): List<Book>

    @Select("SELECT book.book_id, user_id, title, public, created_at, updated_at, rate FROM book INNER JOIN book_rate ON book.book_id = book_rate.book_id WHERE book.public = true ORDER BY book_rate.rate DESC LIMIT 300")
    fun selectBooksWithRate(): List<BookWithRate>

    @Select("SELECT * FROM book LIMIT 300")
    fun selectAll(): List<Book>
}