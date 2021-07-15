package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Word
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface WordMapper {
    @Insert("INSERT INTO word(book_id, word, mean) VALUES(#{bookId}, #{word}, #{mean})")
    fun insert(model: Word): Int

    @Select("SELECT * FROM word WHERE word_id = #{id}")
    fun selectWithId(id: Int): Word?

    @Select("SELECT * FROM word WHERE book_id = #{bookId}")
    fun selectWithBookId(bookId: Int): List<Word>

    @Select("SELECT * FROM word WHERE word LIKE #{pattern} LIMIT 300")
    fun findByWord(pattern: String): List<Word>

    @Select("SELECT * FROM word WHERE mean LIKE #{pattern} LIMIT 300")
    fun findByMean(pattern: String): List<Word>

    @Select("SELECT * FROM word LIMIT 300")
    fun selectAll(): List<Word>

    @Update("UPDATE word SET word = #{word} WHERE word_id = #{id}")
    fun updateWord(id:Int, word: String):Boolean

    @Update("UPDATE word SET mean = #{mean} WHERE word_id = #{id}")
    fun updateMean(id:Int, mean:String):Boolean

    @Update("UPDATE word SET word = #{word}, mean = #{mean} WHERE word_id = #{id}")
    fun update(id:Int, word: String, mean:String):Boolean
}