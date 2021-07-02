package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Book
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

    @Select("SELECT * FROM book LIMIT 300")
    fun selectAll(): List<Book>
}