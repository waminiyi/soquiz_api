package com.soquiz.models

import kotlinx.serialization.Serializable

@Serializable
data class QuestionWithDetails(
    val questionId: String,
    val categoryId: String,
    val topicId: String,
    val language: Language,
    val difficulty: Difficulty,
    val questionText: String,
    val propositions: List<Proposition>
)









