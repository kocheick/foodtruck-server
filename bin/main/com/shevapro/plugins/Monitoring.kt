package com.shevapro.plugins

import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureMonitoring() {
    install(CallLogging) {
       // level = Level.TRACE
       // filter { call -> call.request.path().startsWith("/") }
//        format { call ->
//            val status = call.response.status()
//            val httpMethod = call.request.httpMethod.value
//            val userAgent = call.request.headers["User-Agent"]
//            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
//        }
    }

}
