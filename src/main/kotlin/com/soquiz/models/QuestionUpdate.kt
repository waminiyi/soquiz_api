package com.soquiz.models

import com.soquiz.models.Difficulty
import com.soquiz.models.Language
import com.soquiz.models.Proposition
import kotlinx.serialization.Serializable

@Serializable
data class QuestionUpdate(
    val categoryId: String? = null,
    val topicId: String? = null,
    val language: Language? = null,
    val difficulty: Difficulty? = null,
    val questionText: String? = null,
    val propositions: List<Proposition>? = null
)
