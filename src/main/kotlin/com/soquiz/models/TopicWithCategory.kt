package com.soquiz.models

import kotlinx.serialization.Serializable

@Serializable

data class TopicWithCategory(
    val categoryId: String,
    val topicId: String,
    val name: String
)

@Serializable
data class Topic(
    val topicId: String,
    val topicName: String,
)
