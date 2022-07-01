package com.shevapro.plugins

import com.shevapro.data.repository.db.DatabaseFactory
import com.shevapro.routes.mobile.mobileAppRoute
import com.shevapro.routes.webapp.webAppRoute
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.coroutines.MainScope
import org.koin.ktor.ext.inject

const val API_VERSION = "/api/v1"

fun Application.configureRouting() {
    // init Database
    DatabaseFactory.init()
    val placesService: PlacesService by inject()
    val userService: UserService by inject()

    routing {
        //  placesDB.initSample()
        static("static-resources") {
            resources("templates/css")
            resources("templates/js")

        }

//        this@configureRouting.webAppRoute(userService, placesService)
        this@configureRouting.mobileAppRoute(userService, placesService)

    }

}




