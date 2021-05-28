package jp.ac.gunmau.andolab.mew.service


import jp.ac.gunmau.andolab.mew.mapper.UserMapper
import jp.ac.gunmau.andolab.mew.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserService @Autowired constructor(private val dao: UserMapper): UserDetailsService{

    fun insert(user: User):Boolean{
        return dao.insert(user)>0
    }

    fun select(id:Int):User?{
        return dao.selectWithId(id)
    }

    fun select(nameId:String): User?{
        return dao.selectWithNameId(nameId)
    }

    fun findByName(pattern: String): List<User>{
        return dao.findByName(pattern)
    }

    fun selectAll(): List<User>{
        return dao.selectAll()
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        select(username).let {
            it?:throw UsernameNotFoundException(username)
            return it
        }
    }
}