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

    fun selectByRateId(rateId: Int): Rate{
        return dao.selectWithRateId(rateId)
    }

    fun selectByBookId(bookId: Int): List<Rate>{
        return dao.selectWithBookId(bookId)
    }

    fun selectByUserId(userId: Int): List<Rate>{
        return dao.selectWithUserId(userId)
    }

    fun getAverage(bookId: Int): Double?{
        return dao.selectAvgRate(bookId)
    }

    fun selectAll(): List<Rate>{
        return dao.selectAll()
    }
}