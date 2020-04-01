package com.casair.cba_work.security.jwt

import com.casair.cba_work.domain.User
import com.casair.cba_work.dto.AppLoginDTO
import com.casair.cba_work.dto.UserDTO
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.jhipster.config.JHipsterProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.LinkedHashMap

@Component
class TokenProvider(private val jHipsterProperties: JHipsterProperties) : InitializingBean {
    private val log = LoggerFactory.getLogger(TokenProvider::class.java)
    private var key: Key? = null
    private var tokenValidityInMilliseconds: Long = 0
    private var tokenValidityInMillisecondsForRememberMe: Long = 0

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        val keyBytes: ByteArray
        val secret = jHipsterProperties.security.authentication.jwt.secret
        keyBytes = if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                    "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.")
            secret.toByteArray(StandardCharsets.UTF_8)
        } else {
            log.debug("Using a Base64-encoded JWT secret key")
            Decoders.BASE64.decode(jHipsterProperties.security.authentication.jwt.base64Secret)
        }
        key = Keys.hmacShaKeyFor(keyBytes)
        tokenValidityInMilliseconds = 1000 * jHipsterProperties.security.authentication.jwt.tokenValidityInSeconds
        tokenValidityInMillisecondsForRememberMe = 1000 * jHipsterProperties.security.authentication.jwt
                .tokenValidityInSecondsForRememberMe
    }

    fun createToken(authentication: Authentication, rememberMe: Boolean): String {
        val authorities = authentication.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(","))
        val now = Date().time
        val validity: Date
        validity = if (rememberMe) {
            Date(now + tokenValidityInMillisecondsForRememberMe)
        } else {
            Date(now + tokenValidityInMilliseconds)
        }
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(JWT_USER, authentication.principal)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .body
        val authorities: Collection<GrantedAuthority> = Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())

//        User principal = new User(claims.getSubject(), "", authorities);
        val map = claims[JWT_USER] as LinkedHashMap<String,String>
        map.map { AppLoginDTO(it.key,it.value) }
        val mapper = ObjectMapper()
        val jwtUser: AppLoginDTO = mapper.convertValue(claims[JWT_USER], AppLoginDTO::class.java)


        return UsernamePasswordAuthenticationToken(jwtUser, token, authorities)
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken)
            return true
        } catch (e: SecurityException) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.")
            log.trace("Expired JWT token trace: {}", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT token.")
            log.trace("Unsupported JWT token trace: {}", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT token compact of handler are invalid.")
            log.trace("JWT token compact of handler are invalid trace: {}", e)
        }
        return false
    }

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val JWT_USER = "jwtUser"
    }

}