package jp.ac.gunmau.andolab.mew.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

data class User(var userId:Int?, var nameId:String?, var name:String?, private var password:String?, var createdAt: LocalDateTime?, var updatedAt: LocalDateTime?): UserDetails {

    companion object{
        private val nameIdRegex = Regex("""^\w{5,15}$""")
        private val nameRegex = Regex("""^[0-9a-zA-Zぁ-んァ-ヶ一-龠々ー]{1,50}$""")
        private val passwordRegex = Regex("""^.{6,}""")
    }

    // TODO("権限未実装")
    private val roles: MutableList<String> = mutableListOf("USER")

    constructor(): this(null, null, null, null, null, null)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) }.toMutableList()
    }

    override fun getPassword(): String {
        return password ?: throw NullPointerException("password == null")
    }

    fun setPassword(password: String){
        this.password = password
    }

    override fun getUsername(): String {
        return nameId ?: throw NullPointerException("nameId == null")
    }

    override fun isAccountNonExpired(): Boolean {
        // TODO
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        // TODO
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        // TODO
        return true
    }

    override fun isEnabled(): Boolean {
        // TODO
        return true
    }

    fun validateOnSignup(): Boolean{
        name ?: return false
        nameId ?: return false
        password ?: return false
        if(!name!!.matches(nameRegex))
            return false
        if(!nameId!!.matches(nameIdRegex))
            return false
        if(!password!!.matches(passwordRegex))
            return false

        return true
    }

}