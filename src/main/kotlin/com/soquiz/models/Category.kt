package com.soquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val categoryId: String,
    val name: String
)

