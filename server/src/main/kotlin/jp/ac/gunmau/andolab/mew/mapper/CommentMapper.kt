package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Comment
import jp.ac.gunmau.andolab.mew.model.CommentAndUserName
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CommentMapper {
    @Insert("INSERT INTO comment(book_id, user_id, comment) VALUES(#{bookId}, #{userId}, #{comment})")
    fun insert(model: Comment): Int

    @Select("SELECT * FROM comment WHERE comment_id = #{id}")
    fun selectWithId(id: Int): Comment?

    @Select("SELECT comment.comment_id, comment.book_id, comment.user_id, user.name, comment.comment, comment.created_at FROM comment LEFT OUTER JOIN user ON comment.user_id = user.user_id WHERE comment.book_id = #{bookId}")
    fun selectWithBookId(bookId: Int): List<CommentAndUserName>

    @Select("SELECT comment.comment_id, comment.book_id, comment.user_id, user.name, comment.comment, comment.created_at FROM comment LEFT OUTER JOIN user ON comment.user_id = user.user_id WHERE comment.user_id = #{userId}")
    fun selectWithUserId(userId: Int): List<CommentAndUserName>

    @Select("SELECT * FROM comment LIMIT 300")
    fun selectAll(): List<Comment>
}