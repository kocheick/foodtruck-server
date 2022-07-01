package com.shevapro.routes.mobile

import com.shevapro.cleanNumber
import com.shevapro.data.models.Position
import com.shevapro.data.models.UserSession
import com.shevapro.data.models.UsersPlaces.user
import com.shevapro.data.places
import com.shevapro.services.PlacesService
import com.shevapro.toUUID
import com.shevapro.verifyCode
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.util.*

fun Application.mobileAppRoute(placesService: PlacesService) {
    routing {
        route("/mobile/places"){
            get {
                call.respond(placesService.getPlaces())
            }


            post {
                val param = call.receiveParameters()
                val street =
                    param["street"] ?: return@post call.respondText("""Missing "street" field""")
                val crossStreet =
                    param["cross_street"]
                        ?: return@post call.respondText("""Missing "cross street"  field""")
                val position = Position.values().find {
                    it.name.lowercase() == param["position"]?.lowercase()
                }?.name ?: "N_A"
                val latitude =
                    param["latitude"]?.cleanNumber()
                        ?: return@post call.respondText("""Missing "latitude" field""")
                val longitude =
                    param["longitude"]?.cleanNumber()
                        ?: return@post call.respondText("""Missing "longitude" field""")

                val id = param["placeId"]

                    try {
                        if (id != null) {
                            placesService.updatePlace(
                                id.toUUID(), street,
                                crossStreet, position,
                                latitude, longitude
                            )
                            val places = placesService.getPlaces().sortedByDescending { it.id }

                            call.respond(HttpStatusCode.OK,places)
                        } else {
                            placesService.addNewPlace(street, crossStreet, position, latitude, longitude, UUID.fromString("3b91fe2e-bca6-4eb0-879e-d9b710e61e7d"))
                            call.respond(HttpStatusCode.Accepted,placesService.getPlaces())

                        }


                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        call.respond(e.localizedMessage)

                    }


            }
        }
    }
}