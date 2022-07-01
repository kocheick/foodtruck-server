package com.shevapro.plugins

import com.shevapro.data.models.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.maxAgeInSeconds = 60 * 60 // 1h
            cookie.httpOnly = true
        }

    }
}