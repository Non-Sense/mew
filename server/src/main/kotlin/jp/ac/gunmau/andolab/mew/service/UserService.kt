package jp.ac.gunmau.andolab.mew.service


import jp.ac.gunmau.andolab.mew.mapper.UserMapper
import jp.ac.gunmau.andolab.mew.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val dao: UserMapper){

    fun insert(user: User):Boolean{
        return dao.insert(user)>0
    }

    fun select(id:Int):User{
        return dao.selectWithId(id)
    }

    fun select(nameId:String): User{
        return dao.selectWithNameId(nameId)
    }

    fun selectAll(): List<User>{
        return dao.selectAll()
    }
}