package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class User(var userId:Int?, var nameId:String?, var name:String?, var password:String?, var createdAt: LocalDateTime?, var updateAt: LocalDateTime?)