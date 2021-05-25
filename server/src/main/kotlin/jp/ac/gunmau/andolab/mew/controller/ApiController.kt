package jp.ac.gunmau.andolab.mew.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController {
    @RequestMapping("/")
    fun index():String{
        return "Hello"
    }

    @RequestMapping("/test")
    fun unk():String{
        return "test"
    }
}