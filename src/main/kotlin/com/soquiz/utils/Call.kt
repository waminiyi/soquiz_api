package com.soquiz.utils

import com.soquiz.models.Role
import com.soquiz.utils.Constant.ROLE
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun RoutingCall.isAdminCall(): Boolean {
    val principal = this.principal<JWTPrincipal>()
    val role = principal?.getClaim(ROLE, String::class)
    return role == Role.admin.name
}

fun RoutingCall.isEditorCall(): Boolean {
    val principal = this.principal<JWTPrincipal>()
    val role = principal?.getClaim(ROLE, String::class)
    return role == Role.editor.name
}

suspend fun RoutingCall.respondForbidden() =
    this.respond(HttpStatusCode.Forbidden, "You do not have permission to perform this action.")
