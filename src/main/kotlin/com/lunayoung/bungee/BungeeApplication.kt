package com.lunayoung.bungee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BungeeApplication
    fun main() {
        runApplication<BungeeApplication>()
        print("로그인 에러 픽스 테스트")
    }
