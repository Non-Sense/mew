package jp.ac.gunmau.andolab.mew.model

import java.time.LocalDateTime

data class UserView(var userId:Int?, var nameId:String?, var name:String?, var createdAt: LocalDateTime?, var updatedAt: LocalDateTime?){
    constructor(user:User):this(user.userId,user.nameId,user.name,user.createdAt,user.updatedAt)
}
