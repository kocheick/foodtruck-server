package com.shevapro.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.shevapro.data.models.User
import java.util.*

class JWTService {
    private val issuer = "com.shevapro.truckfinder" //environment.config.property("jwt.issuer").getString()
    private val audience = System.getenv("JWT_AUDIENCE").toString()
//    private val jwtSecret = env.config.property("jwt.secret").getString()


    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("userId", user.id.toString())
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) // 24 hours
}