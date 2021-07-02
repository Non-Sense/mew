package jp.ac.gunmau.andolab.mew.model

data class Rate(
    val rateId: Int?,
    val bookId: Int,
    var userId: Int?,
    val rate: Int
)