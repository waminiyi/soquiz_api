package com.soquiz.plugins.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.logging.*
import java.util.logging.Level
import java.util.logging.Logger

fun Application.configureSecurity() {
    val secretKey = System.getenv("JWT_SECRET")
    val audience = environment.config.property("jwt.audience").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val realmText = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        println("POSTGRES_USER: ${System.getenv("POSTGRES_USER")}")
        println("POSTGRES_URL: ${System.getenv("POSTGRES_URL")}")

        println("POSTGRES_PASS: ${System.getenv("POSTGRES_PASSWORD")}")

        println("JWT: ${System.getenv("JWT_SECRET")}")

        jwt("auth-jwt") {
            realm = realmText
            verifier(
                JWT.require(Algorithm.HMAC256(secretKey))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
