package jp.ac.gunmau.andolab.mew.controller

import jp.ac.gunmau.andolab.mew.model.User
import jp.ac.gunmau.andolab.mew.model.UserView
import jp.ac.gunmau.andolab.mew.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.lang.NullPointerException

@RestController
@RequestMapping("/api")
class ApiController @Autowired constructor(private val userService: UserService){

    companion object{
        private val bcryptRegex = Regex("""^\$2[ayb]\$.{56}$""")
    }

    @RequestMapping
    fun index():String{
        return "Hello"
    }

    @RequestMapping("/test")
    fun test():String{
        return "valid"
    }

    /**
     * ユーザ追加 (POST: /signup)
     *
     * e.g. {"nameId":"testuser","name":"The User","password":"testpassword"}
     * パスワードハッシュはbcryptを使うけどハッシュ化するのはサーバ側にします(ログインするときに平文でパスワード送るので)
     * すでにBCryptだったらそのまま採用します
     */
    @PostMapping("/signup")
    @ResponseBody
    fun addUser(@RequestBody user: User): ResponseEntity<String>{
        if(!user.validateOnSignup())
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        if(!user.password.matches(bcryptRegex)) {
            val encoder = BCryptPasswordEncoder()
            user.password = encoder.encode(user.password)
        }
        try {
            userService.insert(user)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        }
        return ResponseEntity(null, HttpStatus.OK)
    }

    /**
     * ユーザ取得 (GET: /user?[nemeid|id]=)
     *
     * nameidは一意な名前, idは内部ID
     * 完全一致したユーザを返す
     */
    @GetMapping("/user")
    fun findUserByNameId(@RequestParam(name="nameid",required = false) nameId:String?,
                         @RequestParam(name="id",required = false) id:Int?) :ResponseEntity<UserView>{

        nameId?:id?:return ResponseEntity(null,HttpStatus.BAD_REQUEST)

        val p: (User?)->ResponseEntity<UserView> = {
            if(it == null)
                ResponseEntity(null, HttpStatus.NOT_FOUND)
            else
                ResponseEntity(UserView(it), HttpStatus.OK)
        }
        if(nameId!=null){
            userService.select(nameId).let {
                return p(it)
            }
        }
        userService.select(id!!).let {
            return p(it)
        }
    }


    /**
     * ユーザ検索 (GET: /finduser?name=)
     *
     * 表示名で検索して返す
     * あいまい検索になっているはず
     */
    @GetMapping("/finduser")
    fun findUserById(@RequestParam(name="name",required = true) name:String):ResponseEntity<List<UserView>>{
        var q = name
        if(!name.startsWith('%') && !name.endsWith('%'))
            q = "%$name%"
        userService.findByName(q).let {
            if(it.isEmpty())
                return ResponseEntity(listOf(),HttpStatus.NOT_FOUND)
            return ResponseEntity(it.map{ elm->UserView(elm)},HttpStatus.OK)
        }
    }

    /**
     * ユーザ全取得(500件まで)
     *
     * テスト用:そのうち消す
     */
    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<UserView>>{
        return ResponseEntity(userService.selectAll().map{ UserView(it) },HttpStatus.OK)
    }
}