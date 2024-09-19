package com.soquiz.plugins.routing.routes

import com.soquiz.database.mapping.toTopic
import com.soquiz.database.repositories.TopicRepository
import com.soquiz.models.Topic
import com.soquiz.models.TopicWithCategory
import com.soquiz.utils.isAdminCall
import com.soquiz.utils.isEditorCall
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerTopicRoutes() {

    val repository = TopicRepository()

    routing {
        route("/api/v1/topics") {
            get {
                val topics = repository.allTopics()
                call.respond(topics)
            }
            get("/byCategory/{categoryId}") {
                val categoryId = call.parameters["categoryId"]
                if (categoryId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid categoryId")
                    return@get
                }

                try {
                    val topics = repository.allTopicsByCategory(categoryId)
                    if (topics.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "Category not found")
                    } else {
                        call.respond(topics)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
                }

            }
            authenticate("auth-jwt") {
                post {
                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@post
                    }

                    try {
                        val topicRequest = call.receive<TopicWithCategory>()
                        val createdTopic = repository.addTopic(topicRequest)
                        if (createdTopic == null) {
                            call.respond(HttpStatusCode.NotFound, "The topic category doesn't exist.")
                        } else {
                            call.respond(HttpStatusCode.Created, createdTopic.toTopic())
                        }
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                put("{topicId}") {
                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@put
                    }

                    val id = call.parameters["topicId"] ?: return@put call.respond(HttpStatusCode.BadRequest)

                    val updatedTopic = call.receive<Topic>()
                    val result = repository.updateTopic(updatedTopic)

                    if (result != null) {
                        call.respond(HttpStatusCode.OK, "Topic updated successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Topic not found")
                    }
                }

                delete("{topicId}") {
                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@delete
                    }

                    val id = call.parameters["topicId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    val result = repository.removeTopic(id)

                    if (result) {
                        call.respond(HttpStatusCode.OK, "Topic deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Topic not found")
                    }
                }

            }
        }


    }
}