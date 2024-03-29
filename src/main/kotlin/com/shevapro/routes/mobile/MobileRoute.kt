package com.shevapro.routes.mobile

import com.shevapro.plugins.MOBILE_API_URL
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.mobileAppRoute(userService: UserService ,placesService: PlacesService) {

        route(MOBILE_API_URL){
            mobilePlacesRoute(placesService,userService)
            mobileUsersRoute(userService)
            mobileVoteRoute(userService,placesService)
        }
}