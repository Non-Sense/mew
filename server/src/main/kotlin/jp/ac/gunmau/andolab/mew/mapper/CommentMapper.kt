package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Comment
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CommentMapper {
    @Insert("INSERT INTO comment(book_id, user_id, rate, comment) VALUES(#{bookId}, #{userId}, #{rate}, #{comment})")
    fun insert(model: Comment): Int

    @Select("SELECT * FROM comment WHERE comment_id = #{id}")
    fun selectWithId(id: Int): Comment?

    @Select("SELECT * FROM comment WHERE book_id = #{bookId}")
    fun selectWithBookId(bookId: Int): Comment?

    @Select("SELECT * FROM comment WHERE user_id = #{userId}")
    fun selectWithUserId(userId: Int): Comment?

    @Select("SELECT * FROM comment LIMIT 300")
    fun selectAll(): List<Comment>
}