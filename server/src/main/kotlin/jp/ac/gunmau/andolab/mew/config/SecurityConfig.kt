package jp.ac.gunmau.andolab.mew.config

import jp.ac.gunmau.andolab.mew.filter.JwtAuthenticationFilter
import jp.ac.gunmau.andolab.mew.filter.JwtAuthorizationFilter
import jp.ac.gunmau.andolab.mew.JwtProvider
import jp.ac.gunmau.andolab.mew.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityConfig @Autowired constructor(private val provider: JwtProvider, private val userService: UserService):WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        // 静的ファイル群は認証の範囲外
        web.ignoring().antMatchers(
            "/css/**",
            "/js/**"
        )
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .authorizeRequests()
            .antMatchers("/api","/api/signup","/api/login").permitAll()
            .anyRequest().authenticated()
            .and().logout()
            .and().csrf().disable()
            .addFilter(JwtAuthenticationFilter(provider, authenticationManager()))
            .addFilter(JwtAuthorizationFilter(provider, authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Autowired
    fun configureAuth(auth: AuthenticationManagerBuilder){
        auth.userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    fun bCryptPasswordEncoder():BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }

}