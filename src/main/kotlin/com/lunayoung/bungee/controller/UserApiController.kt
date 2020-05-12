package com.lunayoung.bungee.controller

import com.lunayoung.bungee.common.ApiResponse
import com.lunayoung.bungee.domain.auth.SignupRequest
import com.lunayoung.bungee.domain.auth.SignupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserApiController @Autowired constructor(
    private val signupService: SignupService
) {

    @PostMapping("/users")
    fun signup(@RequestBody signupRequest: SignupRequest) =
        ApiResponse.ok(signupService.signup(signupRequest))
    /*
       @RequestBody: 데이터를 http 바디에서 읽음
    * */
}