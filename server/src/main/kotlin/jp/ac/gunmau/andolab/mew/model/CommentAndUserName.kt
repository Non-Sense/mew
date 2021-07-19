package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class CommentAndUserName(
    val commentId:Int,
    val bookId:Int,
    val userId:Int,
    val userName: String,
    val comment:String,
    val createdAt: LocalDateTime)
