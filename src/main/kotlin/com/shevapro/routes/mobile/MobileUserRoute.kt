package com.shevapro.routes.mobile

import com.shevapro.Utils
import com.shevapro.data.models.*
import com.shevapro.services.JWTService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.mobileUsersRoute(userService: UserService) {
    val jwtService: JWTService by inject()
        route("/users") {

            get("/{userid}") {
                val userID = try {
                    call.parameters["userid"]?.toUUID()!!
                } catch (e: Exception) {
                    call.respond(NetworkResult.Error(e.message.toString()))
                    return@get
                }

                val user =
                    userService.getUserByID(userID) ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        NetworkResult.Error("Invalid id")
                    )
                call.respond(user)
            }
            get("/logout") {

//                call.sessions.get<UserSession>() ?: call.respond(HttpStatusCode.NotFound, "User already logged out!")
                call.sessions.clear<UserSession>()
//                call.respondRedirect("/mobile/users/login")
                call.respond(HttpStatusCode.OK)
            }

            post("/register") {
                val u = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
//                if (u != null) call.respondRedirect("/")
                if (u != null) return@post call.respond(HttpStatusCode.Accepted, "User already registered!")

                val request = try {
                    call.receive<SignUpRequest>()
                } catch (e: Exception) {
                    call.respond(e.localizedMessage)
                    return@post
                }

                val isOldUser = userService.doesUserWithEmailExist(request.email)
                if (isOldUser) {
                    call.respond(
                        status = HttpStatusCode.Conflict,
                        NetworkResult.Error("User with email * ${request.email} * already exist.")
                    )
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
                        call.sessions.set(UserSession(newUser.id, sessionCounter))

                        val token = jwtService.generateToken(newUser)
                        call.respond(LoginResponse(newUser.id, token))

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                    }

                }


            }

            post("/login") {
//                val u = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
//                if (u != null) call.respondRedirect("/")
//                if (u != null) return@post call.respond(HttpStatusCode.Accepted, "User already logged in.")
                val request = try {
                    call.receive<LoginRequest>()
                } catch (e: Exception) {
                    call.respond(NetworkResult.Error(e.localizedMessage))
                    return@post
                }

                val user =
                    userService.getUserByEmail(request.email)
                        ?: return@post call.respond(
                            status = HttpStatusCode.NotFound, NetworkResult.Error(
                                """User with email * ${request.email} * does not exist."""
                            )
                        )
                val hashedPassword = Utils.hash(request.password)

                val isValidUser = userService.isValidUser(request.email, hashedPassword)
                if (isValidUser) {
                    val token = try {
                        jwtService.generateToken(user)
                    } catch (e: Exception) {
                        call.respond(NetworkResult.Error(e.localizedMessage))
                        return@post
                    }
                    call.sessions.set(UserSession(user.id, userService.sessionCounter))
                    call.respond(NetworkResult.Success(LoginResponse(user.id, token)))
//                    call.respondRedirect {
//                    path("/places/management")
//                        parameters["user_id"] = user.id.toString()
//                        parameters["token"] = token
//                    }
                } else {
                    call.respond(status = HttpStatusCode.BadRequest, NetworkResult.Error("Invalid credentials."))
                }


            }

        }

}