package com.shevapro.routes.mobile

import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.mobileAppRoute(userService: UserService ,placesService: PlacesService) {
        route("/mobile"){

            mobilePlacesRoute(placesService,userService)
            mobileUsersRoute(userService)
            mobileVoteRoute(userService,placesService)
        }
}