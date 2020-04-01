package com.casair.cba_work.config;

import com.casair.cba_work.security.jwt.JWTConfigurerAdapter
import com.casair.cba_work.security.jwt.TokenProvider
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport

@EnableWebSecurity
@Import(SecurityProblemSupport::class)
class SecurityConfig(val tokenProvider: TokenProvider,val problemSupport: SecurityProblemSupport) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {

        http?.let {
            it.csrf()
                    .disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(problemSupport)
                    .accessDeniedHandler(problemSupport)
                    .and()

                    .headers()
                    .frameOptions()
                    .disable()
                    .and()

                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                    .authorizeRequests()
                    .antMatchers("/app/authenticate").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .apply(JWTConfigurerAdapter(tokenProvider))
        }
    }
}
