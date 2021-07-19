package jp.ac.gunmau.andolab.mew.service

import jp.ac.gunmau.andolab.mew.mapper.RateMapper
import jp.ac.gunmau.andolab.mew.model.Rate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RateService @Autowired constructor(private val dao: RateMapper){
    fun insert(rate: Rate): Boolean{
        return dao.insert(rate)>0
    }

    fun selectByRateId(rateId: Int): Rate?{
        return dao.selectWithRateId(rateId)
    }

    fun selectByBookId(bookId: Int): List<Rate>{
        return dao.selectWithBookId(bookId)
    }

    fun selectByUserId(userId: Int): List<Rate>{
        return dao.selectWithUserId(userId)
    }

    fun selectWithBookIdAndUserId(bookId: Int, userId: Int): Rate?{
        return dao.selectWithBookIdAndUserId(bookId, userId)
    }

    fun updateRate(rateId: Int, rate:Int): Boolean{
        return dao.updateRate(rateId, rate)
    }

    fun updateRate(bookId: Int, userId: Int, rate:Int): Boolean{
        return dao.updateRateWithBookIdAndUserId(bookId, userId, rate)
    }

    fun getAverage(bookId: Int): Double?{
        return dao.selectAvgRate(bookId)
    }

    fun selectAll(): List<Rate>{
        return dao.selectAll()
    }
}