package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class Book(
    var bookId:Int?,
    var userId:Int?,
    var title:String?,
    var public:Boolean?,
    var createdAt:LocalDateTime?,
    var updatedAt:LocalDateTime?)
