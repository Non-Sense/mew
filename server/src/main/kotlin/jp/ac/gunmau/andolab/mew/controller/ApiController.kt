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

    @RequestMapping("/test")
    fun unk():String{
        return "test"
    }

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

    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<User>>{
        return ResponseEntity(userService.selectAll(),HttpStatus.OK)
    }
}