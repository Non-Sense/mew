package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class Word(var wordId:Int, var bookId:Int, var word:String, var mean:String, var createdAt:LocalDateTime, var updatedAt:LocalDateTime)
