package com.shevapro.plugins


import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level
import java.util.logging.Logger

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.TRACE
        filter { call -> call.request.path().startsWith("/") }

    }

}
