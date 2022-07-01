package com.shevapro.routes.mobile

import com.shevapro.data.models.NewPlaceDTO
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

fun Application.mobilePlacesRoute(placesService: PlacesService, userService: UserService) {

    routing {
        route("/mobile/places") {
            get {
//                call.respondText(placesService.getPlaces().toString(), ContentType.Text.Plain, )
                call.respond(HttpStatusCode.OK, placesService.getPlaces())
            }
            authenticate("auth-jwt") {
                post {

                    val params = call.receive<NewPlaceDTO>()
                    val street =
                        params.street ?: return@post call.respondText("""Missing "street" field""")
                    val crossStreet =
                        params.crossStreet ?: return@post call.respondText("""Missing "cross street"  field""")
                    val position = Position.values().find {
                        it.name.lowercase() == (params.position?.name?.lowercase()// ?: param["position"]?.lowercase()
                                )
                    }?.name ?: "N_A"
                    val latitude =
                        params.latitude ?: return@post call.respondText("""Missing "latitude" field""")
                    val longitude =
                        params.longitude ?: return@post call.respondText("""Missing "longitude" field""")

                    val placeId = params.id
                    val userId = params.byUserId
                    try {
                        if (placeId != null) {
                            placesService.getPlaceById(placeId.toUUID()) ?: return@post call.respondText(
                                text = """Place with id "${placeId}" does not exist!""",
                                status = HttpStatusCode.NotFound
                            )
                            placesService.updatePlace(
                                placeId.toUUID(), street,
                                crossStreet, position,
                                latitude, longitude
                            )
                            val places = placesService.getPlaces().sortedByDescending { it.id }

                            call.respond(HttpStatusCode.Accepted, places)
                        } else {
                            placesService.addNewPlace(
                                street,
                                crossStreet,
                                position,
                                latitude,
                                longitude,
                                byUserWithID = userId.toUUID()
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
}