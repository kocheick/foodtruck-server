package com.shevapro.routes.mobile

import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.mobileAppRoute(userService: UserService ,placesService: PlacesService) {

            this@mobileAppRoute.mobilePlacesRoute(placesService,userService)
            this@mobileAppRoute.mobileUsersRoute(userService)
    this@mobileAppRoute.mobileVoteRoute(userService,placesService)

}