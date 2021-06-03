package jp.ac.gunmau.andolab.mew.controller

import jp.ac.gunmau.andolab.mew.model.Book
import jp.ac.gunmau.andolab.mew.model.User
import jp.ac.gunmau.andolab.mew.model.UserView
import jp.ac.gunmau.andolab.mew.service.BookService
import jp.ac.gunmau.andolab.mew.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ApiController @Autowired constructor(
    private val userService: UserService,
    private val bookService: BookService){

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


    private fun <T> responseEntityUtil(obj: T?):ResponseEntity<T>{
        return if(obj == null)
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        else
            ResponseEntity(obj, HttpStatus.OK)
    }
    private fun patternUtil(pattern: String):String{
        if(!pattern.startsWith('%') && !pattern.endsWith('%'))
            return "%$pattern%"
        return pattern
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

        if(nameId!=null){
            userService.select(nameId).let {
                return responseEntityUtil(if(it!=null)UserView(it) else null)
            }
        }
        userService.select(id!!).let {
            return responseEntityUtil(if(it!=null)UserView(it) else null)
        }
    }

    /**
     * ユーザ取得 (GET: /user/{nameid})
     *
     * ログイン用IDでユーザを取得する
     */
    @GetMapping("/user/{nameid}")
    fun getUser(@PathVariable("nameid") nameId: String): ResponseEntity<UserView>{
        userService.select(nameId).let {
            return responseEntityUtil(if(it!=null)UserView(it) else null)
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
        userService.findByName(patternUtil(name)).let {
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

    @PostMapping("/book")
    fun postBook(@RequestBody book:Book): ResponseEntity<String>{
        try {
            bookService.insert(book)
        } catch (e: DuplicateKeyException){
            return ResponseEntity(null, HttpStatus.CONFLICT)
        } catch (e: DataIntegrityViolationException){
            return ResponseEntity(null,HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(null)
    }

    @GetMapping("/book")
    fun getBook(@RequestParam(name="userid",required = true) userId:Int): ResponseEntity<List<Book>>{
        bookService.selectByUserId(userId).let {
            if(it.isEmpty())
                return ResponseEntity(listOf(), HttpStatus.NOT_FOUND)
            return responseEntityUtil(it)
        }
    }

    @GetMapping("/book/{id}")
    fun getBookById(@PathVariable("id") id:Int): ResponseEntity<Book>{
        return responseEntityUtil(bookService.selectById(id))
    }

    @GetMapping("/findbook")
    fun findBook(@RequestParam(name="title",required = true) title:String): ResponseEntity<List<Book>>{
        bookService.findByTitle(patternUtil(title)).let {
            if(it.isEmpty())
                return ResponseEntity(listOf(), HttpStatus.NOT_FOUND)
            return ResponseEntity.ok(it)
        }
    }

    @GetMapping("/books")
    fun getAllBook(): ResponseEntity<List<Book>>{
        return ResponseEntity.ok(bookService.selectAll())
    }
}