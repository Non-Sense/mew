package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class Comment(
    var commentId:Int?,
    var bookId:Int,
    var userId:Int?,
    var comment:String,
    var createdAt:LocalDateTime?)
