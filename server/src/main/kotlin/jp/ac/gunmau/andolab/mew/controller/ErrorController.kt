package jp.ac.gunmau.andolab.mew.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.boot.web.servlet.error.ErrorController as SpringErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/error")
class ErrorController: SpringErrorController {
    override fun getErrorPath(): String {
        return "/error"
    }

    @RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
    fun error(req:HttpServletRequest):ModelAndView{
        val status = getCode(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)?.toString())

        val mav = ModelAndView()
        mav.status = status
        mav.viewName = "error"
        mav.addObject("status", status.value())
        mav.addObject("path", req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI))
        return mav
    }

    @RequestMapping
    fun jsonError(req:HttpServletRequest): ResponseEntity<Map<String, Any>>{
        val status = getCode(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)?.toString())

        val body = HashMap<String, Any>()
        body["status"] = status.value()
        body["path"] = req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)
        return ResponseEntity(body, status)
    }

    private fun getCode(str:String?):HttpStatus{
        try {
            str?.toInt()?.let {
                return HttpStatus.valueOf(it)
            }
        } catch (e: Exception){}
        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}