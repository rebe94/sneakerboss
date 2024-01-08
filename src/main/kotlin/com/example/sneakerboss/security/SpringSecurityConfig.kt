package com.example.sneakerboss.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SpringSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .authorizeRequests()
            .antMatchers("/user/**").authenticated()
            //.antMatchers("//**").hasAnyRole("USER")
            //.anyRequest().authenticated()
                .and()
            .oauth2Login()
                //.defaultSuccessUrl("/home")
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