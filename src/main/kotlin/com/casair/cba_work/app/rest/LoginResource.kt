package com.casair.cba_work.app.rest

import com.casair.cba_work.dto.AppLoginDTO
import com.casair.cba_work.dto.UserDTO
import com.casair.cba_work.security.jwt.TokenProvider
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app")
class LoginResource(val tokenProvider: TokenProvider) {

    @PostMapping("/authenticate")
    fun login(@RequestBody appLoginDTO: AppLoginDTO): ResponseEntity<UserDTO> {
        println("appLoginDTO = $appLoginDTO")
        if (appLoginDTO.userName == "zhangsan" && appLoginDTO.password == "123456") {
            val roles = arrayListOf(SimpleGrantedAuthority("10"))
            val authenticationToken = UsernamePasswordAuthenticationToken(appLoginDTO, "", roles)
            val token = tokenProvider.createToken(authenticationToken, true)
            return ResponseEntity.status(200).body(UserDTO(appLoginDTO.userName, token))
        } else {
            throw Exception("此用户已被禁用")
        }

    }
}