package com.shevapro.plugins

import com.shevapro.data.models.UserSession
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.configureSession(){
    install(Sessions) {
        cookie<UserSession>("user_session"){
            cookie.maxAgeInSeconds = 60 * 60 // 1h
        }

    }
}