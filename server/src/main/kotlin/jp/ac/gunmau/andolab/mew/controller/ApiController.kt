package jp.ac.gunmau.andolab.mew.controller

import jp.ac.gunmau.andolab.mew.model.User
import jp.ac.gunmau.andolab.mew.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
class ApiController @Autowired constructor(private val userService: UserService){
    @RequestMapping("/")
    fun index():String{
        return "Hello"
    }

    /**
     * ユーザ追加 (POST: /user)
     *
     * ex: {"nameId":"testuser","name":"The User","password":"$2a$10$N.8tJRt7h7cp4lEF0J0DTObCrcPKlFQ7hmmvh3ku3NQRi1NcPLDR."}
     * パスワードハッシュはbcryptを使うけどハッシュ化するのはサーバかクライアント側か決めていない
     * とりあえずクライアント側に任せることにする
     */
    @PostMapping("/user")
    @ResponseBody
    fun addUser(@RequestBody user: User): ResponseEntity<String>{
        Logger.getLogger("RestApi").info(user.toString())
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
                         @RequestParam(name="id",required = false) id:Int?) :ResponseEntity<User>{

        nameId?:id?:return ResponseEntity(null,HttpStatus.BAD_REQUEST)

        if(nameId!=null){
            userService.select(nameId).let {
                return ResponseEntity(it, if (it!=null) HttpStatus.OK else HttpStatus.NOT_FOUND)
            }
        }
        userService.select(id!!).let {
            return ResponseEntity(it, if (it!=null) HttpStatus.OK else HttpStatus.NOT_FOUND)
        }
    }


    /**
     * ユーザ検索 (GET: /finduser?name=)
     *
     * 表示名で検索して返す
     * あいまい検索になっているはず
     */
    @GetMapping("/finduser")
    fun findUserById(@RequestParam(name="name",required = true) name:String):ResponseEntity<List<User>>{
        var q = name
        if(!name.startsWith('%') && !name.endsWith('%'))
            q = "%$name%"
        userService.findByName(q).let {
            if(it.isEmpty())
                return ResponseEntity(it,HttpStatus.NOT_FOUND)
            return ResponseEntity(it,HttpStatus.OK)
        }
    }

    /**
     * ユーザ全取得(500件まで)
     *
     * テスト用:そのうち消す
     */
    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<User>>{
        return ResponseEntity(userService.selectAll(),HttpStatus.OK)
    }
}