package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Book
import jp.ac.gunmau.andolab.mew.model.BookWithRate
import org.apache.ibatis.annotations.*

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

    @Select("SELECT * FROM book WHERE title LIKE #{pattern} AND user_id = #{userId} LIMIT 300")
    fun findByTitle(pattern: String, userId: Int): List<Book>

    @Select("SELECT * FROM book WHERE title LIKE #{pattern} AND public = true LIMIT 300")
    fun findPublicByTitle(pattern: String): List<Book>

    @Select("SELECT book.book_id, user_id, title, public, rate, created_at, updated_at FROM book INNER JOIN book_rate ON book.book_id = book_rate.book_id WHERE book.public = true ORDER BY book_rate.rate DESC LIMIT 300")
    fun selectBooksWithRate(): List<BookWithRate>

    @Select("SELECT * FROM book LIMIT 300")
    fun selectAll(): List<Book>

    @Update("UPDATE book SET title = #{title} WHERE book_id = #{id}")
    fun updateTitle(id: Int, title:String):Boolean

    @Update("UPDATE book SET public = #{public} WHERE book_id = #{id}")
    fun updatePublic(id:Int, public:Boolean):Boolean

    @Update("UPDATE book SET title = #{title}, public = #{public} WHERE book_id = #{id}")
    fun update(id:Int, title:String, public:Boolean):Boolean

    @Delete("DELETE FROM book WHERE book_id = #{bookId}")
    fun delete(bookId: Int):Boolean
}