package com.soquiz.database.mapping

import com.soquiz.database.tables.QuestionDao
import com.soquiz.models.Proposition
import com.soquiz.models.Difficulty
import com.soquiz.models.Language
import com.soquiz.models.QuestionWithDetails

fun QuestionDao.toQuestionWithDetails() = QuestionWithDetails(
    categoryId = this.category.categoryId,
    topicId = this.topic.topicId,
    questionId = this.id.value,
    language = Language.valueOf(this.language),
    difficulty = Difficulty.valueOf(this.difficulty),
    questionText = this.questionText,
    propositions = this.answers.map { Proposition(text = it.answer, isCorrect = it.isCorrect) }

)


