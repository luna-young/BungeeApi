package com.lunayoung.bungee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BungeeApplication
    fun main() {
        runApplication<BungeeApplication>()
        print("!!!jar파일 테스트!!!")
    }
