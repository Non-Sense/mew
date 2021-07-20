package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Word
import org.apache.ibatis.annotations.*

@Mapper
interface WordMapper {
    @Insert("INSERT INTO word(book_id, word, mean, user_id) VALUES(#{bookId}, #{word}, #{mean}, #{userId})")
    fun insert(model: Word): Int

    @Select("SELECT * FROM word WHERE word_id = #{id}")
    fun selectWithId(id: Int): Word?

    @Select("SELECT * FROM word WHERE book_id = #{bookId}")
    fun selectWithBookId(bookId: Int): List<Word>

    @Select("SELECT * FROM word WHERE word LIKE #{pattern} LIMIT 300")
    fun findByWord(pattern: String): List<Word>

    @Select("SELECT * FROM word WHERE mean LIKE #{pattern} LIMIT 300")
    fun findByMean(pattern: String): List<Word>

    @Select("SELECT * FROM word WHERE book_id = #{bookId} AND word LIKE #{pattern} LIMIT 300")
    fun findByWordWithBookId(bookId: Int, pattern: String): List<Word>
    @Select("SELECT * FROM word WHERE book_id = #{bookId} AND mean LIKE #{pattern} LIMIT 300")
    fun findByMeanWithBookId(bookId: Int, pattern: String): List<Word>
    @Select("SELECT * FROM word WHERE book_id = #{bookId} AND (mean LIKE #{mean} OR word LIKE #{word}) LIMIT 300")
    fun findWithBookId(bookId: Int, word: String, mean: String): List<Word>

    @Select("SELECT * FROM word LIMIT 300")
    fun selectAll(): List<Word>

    @Update("UPDATE word SET word = #{word} WHERE word_id = #{id}")
    fun updateWord(id:Int, word: String):Boolean

    @Update("UPDATE word SET mean = #{mean} WHERE word_id = #{id}")
    fun updateMean(id:Int, mean:String):Boolean

    @Update("UPDATE word SET word = #{word}, mean = #{mean} WHERE word_id = #{id}")
    fun update(id:Int, word: String, mean:String):Boolean

    @Delete("DELETE FROM word WHERE word_id = #{wordId}")
    fun delete(wordId: Int): Boolean
}