package jp.ac.gunmau.andolab.mew.mapper

import jp.ac.gunmau.andolab.mew.model.Rate
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface RateMapper {
    @Insert("INSERT INTO rate(book_id, user_id, rate) VALUES(#{bookId}, #{userId}, #{rate})")
    fun insert(model: Rate): Int

    @Select("SELECT * FROM rate WHERE rate_id = #{rateId}")
    fun selectWithRateId(rateId: Int): Rate?

    @Select("SELECT * FROM rate WHERE book_id = #{bookId} AND user_id = #{userId}")
    fun selectWithBookIdAndUserId(bookId: Int, userId: Int): Rate?

    @Select("SELECT * FROM rate WHERE book_id = #{bookId}")
    fun selectWithBookId(bookId: Int): List<Rate>

    @Select("SELECT * FROM rate WHERE user_id = #{userId}")
    fun selectWithUserId(userId: Int): List<Rate>

    @Select("SELECT avg(rate) FROM rate WHERE book_id = #{bookId}")
    fun selectAvgRate(bookId: Int):Double?

    @Select("SELECT * FROM rate LIMIT 300")
    fun selectAll(): List<Rate>

    @Update("UPDATE rate SET rate=#{rate} WHERE rate_id = #{rateId}")
    fun updateRate(id: Int, rate: Int): Boolean

    @Update("UPDATE rate SET rate=#{rate} WHERE book_id = #{bookId} AND user_id = #{userId}")
    fun updateRateWithBookIdAndUserId(bookId: Int, userId: Int, rate: Int): Boolean
}