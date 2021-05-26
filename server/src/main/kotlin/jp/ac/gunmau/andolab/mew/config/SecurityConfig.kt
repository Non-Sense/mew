package jp.ac.gunmau.andolab.mew.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig:WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        // 静的ファイル群は認証の範囲外
        web.ignoring().antMatchers(
            "/css/**",
            "/js/**"
        )
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().permitAll()
            // 認証がいらないページの設定
            //.antMatchers("/**").permitAll()
            // 上のやつ以外は全部認証いる
            //.anyRequest().authenticated()
            .and()
            .csrf().disable()
    }

}