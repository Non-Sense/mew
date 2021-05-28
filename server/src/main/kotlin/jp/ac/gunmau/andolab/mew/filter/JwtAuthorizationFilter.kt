package jp.ac.gunmau.andolab.mew.filter

import jp.ac.gunmau.andolab.mew.JwtProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter constructor(private val provider: JwtProvider, authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader(JwtProvider.HEADER_STRING)
        if (header == null) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    /**
     * トークンが生きているか確認してAuthenticationを返す
     * 死んでたらnull返す
     */
    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = provider.resolveToken(request) ?: return null
        if(!provider.validateToken(token))
            return null
        return provider.getAuthentication(token)
    }
}