@file:Suppress("DuplicatedCode")

package com.shevapro.routes.webapp

import com.shevapro.data.models.UserSession
import com.shevapro.data.models.UsersPlaces.user
import com.shevapro.routes.usersRoutes
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.webAppRoute(userService: UserService, placesService: PlacesService) {
    routing {
        route("/") {
            placesRoutes(placesService,userService)
            usersRoutes(userService)



            get("about") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }

                val data =
                    FreeMarkerContent(
                        "/pages/about.ftl",
                        mapOf("user" to user, "places" to placesService.getPlaces()),
                        null
                    )
                call.respond(data)
            }

            get("/") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
               val data = FreeMarkerContent(
                    "/pages/home.ftl",
                    mapOf("user" to user),
                    null
                )
                call.respond(data)
            }
        }

    }
}