package com.example.sneakerboss.security

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SpringSecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun getBcryptPasswordEncoder() = BCryptPasswordEncoder()

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .authorizeRequests()
            .antMatchers("/userproducts").hasAnyRole("USER")
            //.anyRequest().authenticated()
            .and()
            .oauth2Login()
            .and()
            .logout()
                .logoutUrl("/logout") // specify the logout URL
                //.logoutUrl("https://accounts.google.com/logout") // specify the logout URL
                .logoutSuccessUrl("/")
                //.logoutSuccessUrl("https://accounts.google.com/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
    }
}