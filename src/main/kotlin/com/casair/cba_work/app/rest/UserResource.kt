package com.casair.cba_work.app.rest

import com.casair.cba_work.domain.User
import com.casair.cba_work.service.UserService
import jdk.nashorn.tools.Shell.SUCCESS
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app/user")
class UserResource(var userService: UserService) {

    @GetMapping("/getUser/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<User> {
        return ResponseEntity.status(200).body(userService.getUser(id))
    }
}