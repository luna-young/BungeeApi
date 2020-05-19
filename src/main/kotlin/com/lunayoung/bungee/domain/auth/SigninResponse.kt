package com.lunayoung.bungee.domain.auth

class SigninResponse (
    val token: String,
    val refreshToken: String,
    val userName: String,
    val userId: Long
)