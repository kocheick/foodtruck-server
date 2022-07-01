package com.shevapro.routes.mobile

import com.shevapro.data.models.VoteRequest
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.mobileVoteRoute(userService: UserService, placesService: PlacesService){
    routing {
        route("mobile/places/"){
            post("vote") {
                val voteRequest = try {
                    call.receive<VoteRequest>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                    return@post
                }

                try {
                    val userId = voteRequest.userId.toUUID()
                    val placeId = voteRequest.placeId.toUUID()

                    // check if IDs exist in DB and return null if not
                    placesService.getPlaceById(placeId)
                        ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            """Place with ID "$placeId" does not exist."""
                        )
                    userService.getUserByID(userId)
                        ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            """User with ID "$userId" does not exist."""
                        )

                    userService.addVoteForUser(userId, placeId)
                    call.respond(placesService.getPlaces())

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                }
            }

        }
    }
}