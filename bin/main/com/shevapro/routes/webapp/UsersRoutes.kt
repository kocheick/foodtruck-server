package com.shevapro.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shevapro.Utils
import com.shevapro.data.models.LoginResponse
import com.shevapro.data.models.User
import com.shevapro.data.models.UserPlacesResponse
import com.shevapro.data.models.UserSession
import com.shevapro.data.models.Users.username
import com.shevapro.data.models.LoginRequest
import com.shevapro.data.models.SignUpRequest
import com.shevapro.data.models.UsersPlaces.user
import com.shevapro.data.places
import com.shevapro.exceptions.InvalidFieldException
import com.shevapro.services.JWTService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.usersRoutes(userService: UserService) {

    val jwtService: JWTService by inject()

    routing {
        route("/users") {
            get("/{userid}") {
                val userID = try {
                    call.parameters["userid"]?.toUUID()!!
                } catch (e: Exception) {
                    call.respond(e.localizedMessage)
                    return@get
                }

                val user =
                    userService.getUserByID(userID) ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid id")
                call.respond(user)
            }

            get("{userid}/places") {
                val userID = try {
                    call.parameters["userid"]?.toUUID()!!
                } catch (e: Exception) {
                    call.respond(e.localizedMessage)
                    return@get
                }
                val userplaces = userService.getUserByID(userID)?.favoritePlaces
                val a: UserPlacesResponse =
                    if (userplaces?.isNotEmpty() == true) UserPlacesResponse(
                        userID,
                        userplaces.size,
                        userplaces
                    ) else UserPlacesResponse(
                        total = 0, places_id = emptyList()
                    )
                call.respond(a)

            }
            get("/management") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }

                if (user == null) call.respondRedirect("/users/login") else {
                    val data = FreeMarkerContent(
                        "pages/users.ftl",
                        mapOf("user" to user, "users" to userService.getListOfUsers()),
                        null
                    )
                    call.respond(data)
                }


            }

            get {
                call.respond(userService.getListOfUsers())
            }


            post("/signup") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
                if (user != null) return@post call.respondRedirect("/")

                val request = try {
                    val params = call.receiveParameters()
                    val username = params["username"]
                    val email = params["email"]
                    val password = params["password"]
                    SignUpRequest(email, password, username)
                } catch (e: InvalidFieldException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                    return@post
                } catch (e: Exception) {
                    println(e.localizedMessage)
                    return@post
                }

                val isOldUser = userService.doesUserWithEmailExist(request.email)
                if (isOldUser) {
                    call.respondText("""User with email "${request.email}" already exists!""")
                } else {
                    try {
                        val passwordHash = Utils.hash(request.password)
                        userService.createNewUser(
                            email = request.email,
                            passwordHash = passwordHash,
                            username = request.username
                        )
                        val newUser = userService.getUserByEmail(request.email)!!
                        val sessionCounter = userService.sessionCounter
                        call.sessions.set(UserSession(newUser.id, sessionCounter.inc()))
                        val data = FreeMarkerContent("pages/places.ftl", mapOf("user" to newUser), null)

                        call.respond(HttpStatusCode.Created, data)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                    }

                }
            }
            get("/logout") {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/users/login")
            }

            get("/signup") {
                call.respond(FreeMarkerContent("pages/signup.ftl", null, null))
            }
            get("/login") {
                val user = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
                if (user != null) {
                    return@get call.respondRedirect("/")
                } else {
                    call.respond(FreeMarkerContent("pages/login.ftl", null, null))
                }
            }
            post("/login") {
                val u = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
                if (u != null) call.respondRedirect("/")

                val request = try {
                    val params = call.receiveParameters()
                    val email = params["email"]
                    val password = params["password"]
                    LoginRequest(email, password)
                } catch (e: Exception) {
                    call.respond(e.localizedMessage)
                    return@post
                }

                val user =
                    userService.getUserByEmail(request.email)
                        ?: return@post call.respondText("""User with email "${request.email}" does not exist!""")
                val hashedPassword = Utils.hash(request.password)

                val isValidUser = userService.isValidUser(request.email, hashedPassword)
                if (isValidUser) {
//                    val token = try {
//                        jwtService.generateToken(user)
//                    } catch (e: Exception) {
//                        call.respond(e.localizedMessage)
//                        return@post
//                    }
                    val sessionCounter = userService.sessionCounter
                    call.sessions.set(UserSession(user.id, sessionCounter.inc()))
//                    call.respond(LoginResponse(user.id, token))
                    call.respondRedirect("/places/management")
                } else {
                    call.respondText("Invalid credentials ! Try again", status = HttpStatusCode.Unauthorized)
                }


            }
        }

    }
}
