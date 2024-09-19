package com.soquiz.plugins.routing.routes

import com.soquiz.database.repositories.UserRepository
import com.soquiz.models.LoginUser
import com.soquiz.models.RegisterUser
import com.soquiz.models.UserRole
import com.soquiz.plugins.security.generateToken
import com.soquiz.utils.checkPassword
import com.soquiz.utils.isAdminCall
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerUserRoutes() {
    val secretKey = environment.config.property("jwt.secret").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    val repository = UserRepository()

    routing {
        route("/auth") {
            post("/register") {
                val post = call.receive<RegisterUser>()

                try {
                    repository.registerUser(
                        RegisterUser(
                            name = post.name,
                            email = post.email,
                            password = post.password,
                        )
                    )

                    call.respond(
                        status = HttpStatusCode.Created,
                        message = "User created successfully."
                    )
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
                }
            }

            post("/login") {
                val post = call.receive<LoginUser>()
                val user = UserRepository().getUserByEmail(post.email)

                if (user == null || !checkPassword(post.password, user.password)) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = "Invalid credentials."
                    )
                    return@post
                }

                val token = generateToken(user, secretKey, audience, issuer)
                call.respond(message = hashMapOf("token" to token).toString())
            }
        }
        authenticate("auth-jwt") {

            route("/admin/setRole") {
                post {

                    if (!call.isAdminCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@post
                    }

                    try {
                        val roleRequest = call.receive<UserRole>()
                        val updatedUser = repository.setUserRole(roleRequest)
                        if (updatedUser == null) {
                            call.respond(HttpStatusCode.NotFound, "The user doesn't exist.")
                        } else {
                            call.respond(HttpStatusCode.OK, "User updated successfully.")
                        }
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }

                }
            }
        }
    }
}
