package com.lunayoung.bungee.controller

import com.lunayoung.bungee.common.ApiResponse
import com.lunayoung.bungee.domain.auth.JWTUtil
import com.lunayoung.bungee.domain.auth.SigninRequest
import com.lunayoung.bungee.domain.auth.SigninService
import com.lunayoung.bungee.domain.auth.UserContextHolder
import com.lunayoung.bungee.interceptor.TokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/v1")
class SigninApiController @Autowired constructor(
    private val signinService: SigninService,
    private val userContextHolder: UserContextHolder
) {
    @PostMapping("/signin")
    fun signin(@RequestBody signinRequest: SigninRequest) =
        ApiResponse.ok(signinService.signin(signinRequest))

    @PostMapping("/refresh_token")
    fun refreshToken (
            @RequestParam("grant_type") grantType: String
    ) : ApiResponse {
        if(grantType != TokenValidationInterceptor.GRANT_TYPE_REFRESH) {
            throw IllegalArgumentException("grant_type 없음")
        }
        return  userContextHolder.email?.let {
            ApiResponse.ok(JWTUtil.createToken(it))
        }?: throw IllegalArgumentException("사용자 정보 없음")
    }

}