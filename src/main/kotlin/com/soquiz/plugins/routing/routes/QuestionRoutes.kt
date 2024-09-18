package com.soquiz.plugins.routing.routes

import com.soquiz.database.mapping.toQuestionWithDetails
import com.soquiz.database.repositories.QuestionRepository
import com.soquiz.models.Difficulty
import com.soquiz.models.Language
import com.soquiz.models.QuestionUpdate
import com.soquiz.models.QuestionWithDetails
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerQuestionRoutes() {
    val repository = QuestionRepository()
    routing {
        route("/api/v1/quiz") {
            get {
                get {
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5
                    val language = call.request.queryParameters["language"]?.let { Language.valueOf(it) } ?: Language.en
                    val category = call.request.queryParameters["category"]
                    val topic = call.request.queryParameters["topic"]
                    val difficulty = call.request.queryParameters["difficulty"]?.let { Difficulty.valueOf(it) }

                    try {
                        val questions = repository.getQuestions(
                            limit = limit,
                            language = language,
                            category = category,
                            topic = topic,
                            difficulty = difficulty
                        )
                        call.respond(HttpStatusCode.OK, questions)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid parameters: ${e.message}")
                    }
                }
            }

            get("/{id}") {
                val questionId = call.parameters["id"]
                if (questionId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing question ID")
                    return@get
                }

                val question = repository.getQuestionById(questionId)
                if (question == null) {
                    call.respond(HttpStatusCode.NotFound, "Question not found")
                } else {
                    call.respond(question)
                }
            }

            authenticate("auth-jwt") {
                post {
                    val principal = call.principal<JWTPrincipal>()
                    val role = principal?.getClaim("role", String::class)

                    if (role != "admin" && role != "editor") {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@post
                    }

                    try {
                        val post = call.receive<QuestionWithDetails>()
                        repository.addQuestion(post)
                        call.respond(HttpStatusCode.Created)

                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                patch("/{id}") {
                    val principal = call.principal<JWTPrincipal>()
                    val role = principal?.getClaim("role", String::class)

                    if (role != "admin" && role != "editor") {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@patch
                    }

                    val questionId = call.parameters["id"] ?: return@patch call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing question ID"
                    )

                    try {
                        val updateRequest = call.receive<QuestionUpdate>()
                        val updatedQuestion = repository.updateQuestion(questionId, updateRequest)
                        call.respond(HttpStatusCode.OK, updatedQuestion.toQuestionWithDetails())

                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }

                }
            }

            delete("{topicId}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim("role", String::class)

                if (role != "admin" && role != "editor") {
                    call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                    return@delete
                }

                val questionId = call.parameters["id"]
                if (questionId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing question ID")
                    return@delete
                }

                val result = repository.removeQuestion(questionId)

                if (result) {
                    call.respond(HttpStatusCode.OK, "Question deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Question not found")
                }
            }

        }
    }


}