package com.shevapro

import com.shevapro.di.mainModule
import com.shevapro.plugins.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        SLF4JLogger()
        modules(mainModule)
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)

        allowHeader(Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    configureSecurity()
    configureSession()
    configureRouting()
    configureSerialization()
    configureMonitoring()
}