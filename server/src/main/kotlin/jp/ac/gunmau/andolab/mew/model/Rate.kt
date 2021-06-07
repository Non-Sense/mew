package jp.ac.gunmau.andolab.mew.model

data class Rate(
    val rateId: Int?,
    val bookId: Int,
    val userId: Int,
    val rate: Int
)