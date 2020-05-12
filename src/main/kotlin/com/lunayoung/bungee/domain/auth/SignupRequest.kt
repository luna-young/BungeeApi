package com.lunayoung.bungee.domain.auth

import com.lunayoung.bungee.common.BungeeException

data class SignupRequest(
        val email: String,
        val name: String,
        val password: String
) {


}