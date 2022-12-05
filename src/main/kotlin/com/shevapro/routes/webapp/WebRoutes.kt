@file:Suppress("DuplicatedCode")

package com.shevapro.routes.webapp

import com.shevapro.data.models.UserSession
import com.shevapro.routes.usersRoutes
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.webAppRoute(userService: UserService, placesService: PlacesService) {
    routing {
        route("/") {
            this@webAppRoute.placesRoutes(placesService, userService)
            this@webAppRoute.usersRoutes(userService)


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