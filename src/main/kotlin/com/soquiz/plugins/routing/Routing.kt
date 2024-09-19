package com.soquiz.plugins.routing

import com.soquiz.plugins.routing.routes.registerCategoryRoutes
import com.soquiz.plugins.routing.routes.registerQuestionRoutes
import com.soquiz.plugins.routing.routes.registerTopicRoutes
import com.soquiz.plugins.routing.routes.registerUserRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        registerUserRoutes()
        registerCategoryRoutes()
        registerTopicRoutes()
        registerQuestionRoutes()
    }
}
