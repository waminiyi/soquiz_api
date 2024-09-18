package com.soquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class Proposition(
    val text: String,
    val isCorrect: Boolean
)

