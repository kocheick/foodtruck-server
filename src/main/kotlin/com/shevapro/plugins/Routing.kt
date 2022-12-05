package com.shevapro.plugins

import com.shevapro.data.repository.db.DatabaseFactory
import com.shevapro.routes.mobile.mobileAppRoute
import com.shevapro.routes.mobile.mobilePlacesRoute
import com.shevapro.routes.mobile.mobileUsersRoute
import com.shevapro.routes.mobile.mobileVoteRoute
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

const val API_VERSION = "/api/v1"
const val MOBILE_API_URL = "$API_VERSION/mobile"


fun Application.configureRouting() {
    // init Database
    try {
        DatabaseFactory.init()
    } catch (e: Throwable) {
        println("DB factory - error from init()  : ${e.localizedMessage} ")
    }
    val placesService: PlacesService by inject()
    val userService: UserService by inject()

    routing {
        //  placesDB.initSample()
        static("static-resources") {
            resources("templates/css")
            resources("templates/js")

        }
//        this@configureRouting.webAppRoute(userService, placesService)
//        this@configureRouting.mobileAppRoute(userService, placesService)

        mobileAppRoute(userService, placesService)



    }
}


