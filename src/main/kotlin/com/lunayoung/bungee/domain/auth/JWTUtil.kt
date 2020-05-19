package com.lunayoung.bungee.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

//하는 일이 고정적이고 다른 코드에 영향을 주지 않기 때문에 싱글턴으로 선언
object JWTUtil {

    private const val ISSUER = "Bungee"
    private const val SUBJECT = "Auth"
    private const val EXPIRE_TIME = 60L*60*2*1000 //2시간
    private const val REFRESH_EXPIRE_TIME = 60L*60*24*30*1000 //30일

    private val SECRET = "your-secret"
    private val algorithm: Algorithm = Algorithm.HMAC256(SECRET)

    private val refreshSecret = "your-refresh-secret"
    private val refreshAlgorithm: Algorithm = Algorithm.HMAC256(refreshSecret)

    fun createToken(email:String) = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(Date())
        .withExpiresAt(Date(Date().time+ EXPIRE_TIME))
        .withClaim(JWTClaims.EMAIL, email)
        .sign(algorithm)

    fun createRefreshToken(email:String) = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(Date())
        .withExpiresAt(Date(Date().time+ REFRESH_EXPIRE_TIME))
        .withClaim(JWTClaims.EMAIL, email)
        .sign(refreshAlgorithm)


    object JWTClaims {
        const val EMAIL = "email"
    }
}