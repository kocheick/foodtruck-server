package com.shevapro.routes.mobile

import com.shevapro.Utils
import com.shevapro.data.models.LoginRequest
import com.shevapro.data.models.LoginResponse
import com.shevapro.data.models.UserSession
import com.shevapro.data.models.Users
import com.shevapro.services.JWTService
import com.shevapro.services.UserService
import com.shevapro.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Application.mobileUsersRoute(userService:UserService){
    val jwtService : JWTService by inject()
    routing {
        route("mobile/users"){

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
            get("/logout") {

                call.sessions.get<UserSession>() ?: call.respond(HttpStatusCode.NotFound,"User already logged out!")
                call.sessions.clear<UserSession>()
//                call.respondRedirect("/mobile/users/login")
                call.respond(HttpStatusCode.Accepted)
            }

            post("/login") {
                val u = call.sessions.get<UserSession>()?.let { userService.getUserByID(it.userId) }
//                if (u != null) call.respondRedirect("/")
                if (u!=null) return@post call.respond(HttpStatusCode.Accepted,"User already logged in!")

                val request = try {
                    call.receive<LoginRequest>()
                } catch (e: Exception) {
                    call.respond(e.localizedMessage)
                    return@post
                }

                val user =
                    userService.getUserByEmail(request.email)
                        ?: return@post call.respondText(text="""User with email "${request.email}" does not exist!""", status = HttpStatusCode.NotFound)
                val hashedPassword = Utils.hash(request.password)

                val isValidUser = userService.isValidUser(request.email, hashedPassword)
                if (isValidUser) {
                    val token = try {
                        jwtService.generateToken(user)
                    } catch (e: Exception) {
                        call.respond(e.localizedMessage)
                        return@post
                    }
                    call.sessions.set(UserSession(user.id,userService.sessionCounter ))
                    call.respond(LoginResponse(user.id, token))
//                    call.respondRedirect {
//                    path("/places/management")
//                        parameters["user_id"] = user.id.toString()
//                        parameters["token"] = token
//                    }
                } else {
                    call.respondText("Invalid credentials ! Try again", status = HttpStatusCode.Unauthorized)
                }


            }

        }
    }
}