package com.casair.cba_work.security.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JWTConfigurerAdapter(private val tokenProvider: TokenProvider) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity?) {
        super.configure(builder)
        val jwtFilter = JWTFilter(tokenProvider)
        builder?.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}