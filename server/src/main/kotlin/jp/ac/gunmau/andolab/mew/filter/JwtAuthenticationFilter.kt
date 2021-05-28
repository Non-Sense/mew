package jp.ac.gunmau.andolab.mew.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jp.ac.gunmau.andolab.mew.JwtProvider
import jp.ac.gunmau.andolab.mew.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.logging.Logger
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(private val provider: JwtProvider, private val manager: AuthenticationManager)
    : UsernamePasswordAuthenticationFilter() {

    companion object {
        private val LOGGER = Logger.getLogger("JwtAuthentication")
    }

    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/api/login", "POST"))
    }

    /**
     * 認証処理
     */
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        return try {
            // リクエストのJsonをパースしてspring securityの認証に投げる
            val user: User = ObjectMapper().readValue(req.inputStream, User::class.java)
            manager.authenticate(
                UsernamePasswordAuthenticationToken(
                    user.username,
                    user.password,
                    user.authorities
                )
            )
        } catch (e: IOException) {
            LOGGER.warning(e.message)
            throw RuntimeException(e)
        }
    }

    /**
     * 認証成功時の処理
     */
    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest?,
        res: HttpServletResponse,
        chain: FilterChain?,
        auth: Authentication
    ) {
        if(res.isCommitted)
            return
        // トークン作ってヘッダに投げる
        val token: String = provider.createToken(auth.principal as User)
        res.addHeader(JwtProvider.HEADER_STRING, token)
        res.status = HttpStatus.OK.value()
    }


}