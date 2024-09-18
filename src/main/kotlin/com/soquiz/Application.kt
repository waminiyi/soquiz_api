package com.soquiz

import com.soquiz.plugins.*
import com.soquiz.plugins.routing.configureRouting
import com.soquiz.plugins.security.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    initializeDatabase()
    configureSecurity()
    configureRouting()
}
