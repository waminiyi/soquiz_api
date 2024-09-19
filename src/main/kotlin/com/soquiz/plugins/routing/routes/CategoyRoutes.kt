package com.soquiz.plugins.routing.routes

import com.soquiz.database.mapping.toCategory
import com.soquiz.database.repositories.CategoryRepository
import com.soquiz.models.Category
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

fun Application.registerCategoryRoutes() {
    val repository = CategoryRepository()
    routing {
        route("/api/v1/categories") {
            get {
                val categories = repository.allCategories()
                call.respond(categories)
            }
            authenticate("auth-jwt") {
                post {
                    val principal = call.principal<JWTPrincipal>()
                    val role = principal?.getClaim("role", String::class)

                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@post
                    }

                    try {
                        val categoryRequest = call.receive<Category>()
                        val createdCategory = repository.addCategory(categoryRequest)
                        call.respond(HttpStatusCode.Created, createdCategory.toCategory())
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                put("{categoryId}") {
                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@put
                    }

                    val id = call.parameters["categoryId"] ?: return@put call.respond(HttpStatusCode.BadRequest)

                    val updatedCategory = call.receive<Category>()
                    val result = repository.updateCategory(updatedCategory)

                    if (result != null) {
                        call.respond(HttpStatusCode.OK, "Category updated successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Category not found")
                    }
                }

                delete("{categoryId}") {
                    if (!call.isAdminCall() && !call.isEditorCall()) {
                        call.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
                        return@delete
                    }

                    val id = call.parameters["categoryId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    val result = repository.removeCategory(id)

                    if (result) {
                        call.respond(HttpStatusCode.OK, "Category deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Category not found")
                    }
                }

            }
        }


    }
}