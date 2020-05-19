package com.lunayoung.bungee.domain.auth

//로그인에 필요한 데이터는 이메일, 비밀번호뿐
data class SigninRequest (
    val email: String,
    val password: String
)