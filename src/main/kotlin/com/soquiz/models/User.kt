package com.soquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUser(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class UserRole(
    val email: String,
    val role: String
)

@Serializable
data class LoginUser(
    val email: String,
    val password: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val password: String
)

