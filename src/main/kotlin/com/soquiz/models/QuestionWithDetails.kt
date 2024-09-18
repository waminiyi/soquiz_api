package com.soquiz.models

import com.soquiz.models.Difficulty
import com.soquiz.models.Language
import com.soquiz.models.Proposition
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









