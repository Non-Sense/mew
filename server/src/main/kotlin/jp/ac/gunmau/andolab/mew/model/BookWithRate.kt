package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class BookWithRate(
    var bookId:Int?,
    var userId:Int?,
    var title:String,
    var public:Boolean = false,
    var rate:Double?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?)
