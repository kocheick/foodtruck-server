package com.shevapro.plugins

import io.ktor.server.plugins.*
import org.slf4j.event.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.plugins.callloging.CallLogging

fun Application.configureMonitoring() {
    install(CallLogging) {
        // level = Level.TRACE
        // filter { call -> call.request.path().startsWith("/") }
//        format { call ->
//            val status = call.response.status()
//            val httpMethod = call.request.httpMethod.value
//            val userAgent = call.request.headers["User-Agent"]
//            "Status: $status | $httpMethod| User agent: $userAgent"
//        }
    }

}
