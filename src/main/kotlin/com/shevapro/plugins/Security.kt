package com.shevapro.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shevapro.data.models.UserSession
import com.shevapro.services.JWTService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.exposedLogger
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureSecurity() {
    val myRealm = environment.config.property("jwt.realm").getString()

    val user_service: UserService by inject()
    val jwtService: JWTService by inject()

    install(Authentication) {

        // session for admins
        session<UserSession>("auth-session") {
            validate { session ->
                val id = session.userId
                val isValidUser = user_service.getUserByID(id)
                if (isValidUser != null) {
                    session
                } else {
                    null
                }
            }
        }
        // jtw for mobile api

        jwt("auth-admin-jwt") {
            realm = myRealm
        }

        jwt("auth-jwt") {

            realm = myRealm
            verifier {
                jwtService.verifier
            }

            validate { jwtCredential ->
                val id = jwtCredential.payload.claims["userId"]
                val uuid = UUID.fromString(id.toString().replace("\"", ""))
                val isValidUser = user_service.getUserByID(uuid)
                if (isValidUser != null) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}


