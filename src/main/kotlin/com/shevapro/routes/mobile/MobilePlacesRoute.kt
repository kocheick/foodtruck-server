package com.shevapro.routes.mobile

import com.shevapro.data.models.PlaceDTO
import com.shevapro.data.models.Position
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Route.mobilePlacesRoute(placesService: PlacesService, userService: UserService) {

        route("/places") {

            get {
//                call.respondText(placesService.getPlaces().toString(), ContentType.Text.Plain, )
                val places = placesService.getPlaces()
                call.respond(HttpStatusCode.OK, places)
            }
            authenticate("auth-jwt") {

                get("/clear") {
                    placesService.clearPlaces()
                    call.respond(HttpStatusCode.Accepted)
                }

                delete("{place_id}") {
                    val request = call.parameters
                    val placeToRemove = request["place_id"]?.toUUID() ?: return@delete call.respond(
                        HttpStatusCode.OK,
                        "Parameter place_id is absent"
                    )
                   placesService.getPlaceById(placeToRemove) ?: return@delete  call.respond(
                       HttpStatusCode.NotFound,
                       "Place (ID: $placeToRemove) has been removed or does not exist."
                   )
                        placesService.removePlaceById(placeToRemove)
                        call.respond(HttpStatusCode.OK)

                }

                post {

                    val params = try {
                        call.receive<PlaceDTO>()
                    } catch (e: Throwable) {
                        call.respond(HttpStatusCode.BadRequest)
                        println("${e.message}")
                        call.receive<PlaceDTO>()
                    }
                    val street =
                        params.street ?: return@post call.respondText("""Missing "street" field""")
                    val crossStreet =
                        params.crossStreet ?: return@post call.respondText("""Missing "cross street"  field""")
                    val position = Position.values().find {
                        it.name == ((params.streetPosition?.name?.uppercase()
                            ?: return@post call.respond("Missing street_position field"))
                                )
                    }?.name ?: "N_A"
                    val latitude =
                        params.latitude ?: return@post call.respondText("""Missing "latitude" field""")
                    val longitude =
                        params.longitude ?: return@post call.respondText("""Missing "longitude" field""")

                    val hours = params.businessHours

                    val placeId = params.id
                    val userId = params.byUserId.toUUID()
//                    userService.getUserByID(userId) ?: return@post call.respondText(text = "User (ID: $userId) does not exist or have permission for current request.", status = HttpStatusCode.NotFound)
                    try {
                        if (!placeId.isNullOrEmpty()) {
                            placesService.getPlaceById(placeId.toUUID()) ?: return@post call.respondText(
                                text = "Place (ID: ${placeId}) does not exist.",
                                status = HttpStatusCode.NotFound
                            )
                                placesService.updatePlace(
                                    placeId.toUUID(), street,
                                    crossStreet, position,
                                    latitude, longitude, hours,userId
                                )


                            val places = placesService.getPlaces().sortedByDescending { it.lastUpdateDate }

                            call.respond(HttpStatusCode.Accepted, places)
                        } else {
                            placesService.addNewPlace(
                                street,
                                crossStreet,
                                position,
                                latitude,
                                longitude,
                                hours, userId
                            )
                            call.respond(HttpStatusCode.Created, placesService.getPlaces())
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        call.respond(e.localizedMessage)
                    }
                }
            }
        }

}