package com.soquiz.plugins.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.soquiz.models.User
import com.soquiz.utils.Constant.ROLE

fun generateToken(user: User, secretKey: String, audience: String, issuer: String): String {
    return JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(user.email)
        .withClaim(ROLE, user.role)
        .sign(Algorithm.HMAC256(secretKey))
}
