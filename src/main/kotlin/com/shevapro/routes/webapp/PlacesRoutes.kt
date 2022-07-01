package com.shevapro.routes.webapp

import com.shevapro.cleanNumber
import com.shevapro.data.models.Position
import com.shevapro.data.models.UserSession
import com.shevapro.data.models.VoteRequest
import com.shevapro.securityCode
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import com.shevapro.verifyCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


fun Application.placesRoutes(placesService: PlacesService, userService: UserService) = routing {
        route("/places") {

            get("{place_id}") {

                val id = call.parameters["place_id"].toString().replace("\"", "")
                val uuid = id.toUUID()

                call.respond(placesService.getPlaceById(uuid) ?: return@get call.respond("Invalid id"))
            }
            get("/management") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
                if (user == null) call.respondRedirect("/users/login")
                else {
                    val date = System.currentTimeMillis()
                    val code = call.securityCode(user, date)
                    val places = placesService.getPlaces().sortedByDescending { it.lastUpdateDate }
                    val data = FreeMarkerContent(
                        "pages/places.ftl",
                        mapOf("places" to places, "user" to user, "code" to code, "date" to date),
                        user.id.toString()
                    )
                    call.respond(data)
                }
            }


            get("/management/clear") {
                call.respond(HttpStatusCode.Accepted, placesService.clearPlaces())
            }

            authenticate("auth-session") {
                post("{place_id}") {
                    val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
                    val param = call.receiveParameters()
                    val date = param["date"]?.toLongOrNull() ?: return@post call.respond("refresh page")
                    val code = param["code"] ?: return@post call.respond("security -- refresh page")

                    if (user == null || !call.verifyCode(date, user, code)) {
                        call.sessions.clear<UserSession>()
                        call.respondRedirect("/users/login")
                    } else {
                        try {
                            val id = param["placeId"]!!.toUUID()
                            placesService.removePlaceById(id)
                            val places = placesService.getPlaces()
                            val data = FreeMarkerContent(
                                "pages/places.ftl",
                                mapOf(
                                    "code" to code,
                                    "date" to date,
                                    "user" to user,
                                    "places" to places
                                ),
                                user.id.toString()
                            )

                            call.respond(data)
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                            call.respond(e.localizedMessage)
                        }


                    }

                }

                post {
                    val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
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


                    val date = param["date"]?.toLongOrNull() ?: return@post call.respond("refresh page")
                    val code = param["code"] ?: return@post call.respond("security -- refresh page")
                    val id = param["placeId"]
                    if (user == null || !call.verifyCode(date, user, code)) {
                        call.sessions.clear<UserSession>()
                        call.respondRedirect("/users/login")
                    } else {
                        try {
                            if (id != null) {
                                placesService.updatePlace(
                                    id.toUUID(), street,
                                    crossStreet, position,
                                    latitude, longitude
                                )
                                val places = placesService.getPlaces().sortedByDescending { it.id }
                                val data = FreeMarkerContent(
                                    "pages/places.ftl",
                                    mapOf(
                                        "code" to code,
                                        "date" to date,
                                        "user" to user,
                                        "places" to places
                                    ),
                                    user.id.toString()
                                )

                                call.respond(HttpStatusCode.Accepted, data)
                            } else {
                                placesService.addNewPlace(street, crossStreet, position, latitude, longitude, user.id)
                                val data = FreeMarkerContent(
                                    "pages/places.ftl",
                                    mapOf(
                                        "code" to code,
                                        "date" to date,
                                        "user" to user,
                                        "places" to placesService.getPlaces().sortedBy { it.id }
                                    ),
                                    user.id.toString()
                                )
                                println("new place added")
                                call.respond(HttpStatusCode.Created, data)
                            }


                        } catch (e: Exception) {
                            println(e.localizedMessage)
                            call.respond(e.localizedMessage)

                        }
                    }

                }


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
                patch {
                    val param = call.receiveParameters()

                    val id =
                        param["id"] ?: return@patch call.respondText("""Missing "id" field""")
                    val street =
                        param["street"] ?: return@patch call.respondText("""Missing "street" field""")
                    val crossStreet =
                        param["cross_street"]
                            ?: return@patch call.respondText("""Missing "cross street"  field""")
                    val position = Position.values().find {
                        it.name.lowercase() == param["position"]?.lowercase()
                    }?.name ?: return@patch call.respondText("""Missing "position" field""")
                    val latitude =
                        param["latitude"]?.toDoubleOrNull()
                            ?: return@patch call.respondText("""Missing "latitude" field""")
                    val longitude =
                        param["longitude"]?.toDoubleOrNull()
                            ?: return@patch call.respondText("""Missing "longitude" field""")

                    try {
                        placesService.updatePlace(
                            id.toUUID(),
                            street,
                            crossStreet,
                            position,
                            latitude,
                            longitude
                        )
                        call.respond(placesService.getPlaces().sortedBy { it.id })
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        call.respond(e.localizedMessage)
                    }
                }
            }
        }
}

