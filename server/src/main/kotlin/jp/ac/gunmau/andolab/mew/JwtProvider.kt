package jp.ac.gunmau.andolab.mew

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jp.ac.gunmau.andolab.mew.model.User
import jp.ac.gunmau.andolab.mew.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtProvider @Autowired constructor(private val service: UserService){

    companion object{
        private const val secretKey = "oPSIXNPOWXcZxfQmGDfx6FKJwjkdUPOnH6e655k1VX7K8pBKUVZCi7lF0CXar4oM"
        private const val validDuration = 1000L*60L*60L*2L
        const val HEADER_STRING = "X-AUTH-TOKEN"
    }

    fun createToken(user:User): String{
        val craims = Jwts.claims().setSubject(user.nameId)
        craims["roles"] = user.authorities
        val iat = Date()
        val exp = Date(iat.time + validDuration)
        return Jwts.builder()
            .setClaims(craims)
            .setIssuedAt(iat)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setHeaderParam("typ","JWT")
            .compact()
    }

    fun getAuthentication(token:String): Authentication{
        val userDetails = service.loadUserByUsername(getSubject(token)!!)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun resolveToken(req:HttpServletRequest):String?{
        return req.getHeader(HEADER_STRING)
    }

    fun validateToken(token: String?): Boolean {
        try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            if(!claims.header.algorithm.equals(SignatureAlgorithm.HS512.value))
                return false
            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }

    fun getSubject(token:String):String?{
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }
}