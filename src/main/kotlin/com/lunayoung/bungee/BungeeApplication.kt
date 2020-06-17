package com.lunayoung.bungee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BungeeApplication
    fun main() {
        runApplication<BungeeApplication>()
        print("bungee v.2.0")
    }
