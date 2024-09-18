package com.soquiz.database.repositories

import com.soquiz.database.mapping.toQuestionWithDetails
import com.soquiz.database.tables.*
import com.soquiz.models.Difficulty
import com.soquiz.models.Language
import com.soquiz.models.QuestionUpdate
import com.soquiz.models.QuestionWithDetails
import com.soquiz.utils.suspendTransaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

class QuestionRepository {

    suspend fun getQuestions(
        limit: Int = 5,
        language: Language = Language.en,
        category: String? = null,
        topic: String? = null,
        difficulty: Difficulty? = null
    ): List<QuestionWithDetails> = suspendTransaction {
        if (limit < 0) throw IllegalArgumentException("Limit must be non-negative")

        QuestionDao.find {
            (QuestionsTable.language eq language.name) and
                    (difficulty?.let { (QuestionsTable.difficulty eq difficulty.name) } ?: Op.TRUE) and
                    (category?.let { (QuestionsTable.category eq category) } ?: Op.TRUE) and
                    (topic?.let { (QuestionsTable.topic eq topic) } ?: Op.TRUE)
        }.shuffled().take(limit)
            .map { it.toQuestionWithDetails() }

    }

    suspend fun getQuestionById(questionId: String): QuestionWithDetails? = suspendTransaction {
        QuestionDao.findById(questionId)?.toQuestionWithDetails()
    }


    suspend fun addQuestion(questionWithDetails: QuestionWithDetails) = suspendTransaction {
        val questionDao = QuestionDao.new(questionWithDetails.questionId) {
            category = CategoryDao.findById(questionWithDetails.categoryId)
                ?: throw IllegalArgumentException("Category not found")
            topic = TopicDao.findById(questionWithDetails.topicId) ?: throw IllegalArgumentException("Topic not found")
            language = questionWithDetails.language.name
            difficulty = questionWithDetails.difficulty.name
            questionText = questionWithDetails.questionText
        }

        questionWithDetails.propositions.forEach { answer ->
            AnswerDao.new {
                this.question = questionDao
                this.answer = answer.text
                this.isCorrect = answer.isCorrect
            }
        }
    }

    suspend fun updateQuestion(questionId: String, update: QuestionUpdate) = suspendTransaction {
        val existingQuestion = QuestionDao.find { QuestionsTable.id eq questionId }.firstOrNull()
            ?: throw IllegalArgumentException("Question not found")
        existingQuestion.apply {
            update.categoryId?.let {
                this.category = CategoryDao.find { CategoriesTable.categoryId eq it }.firstOrNull()
                    ?: throw IllegalArgumentException("Category not found")
            }

            update.topicId?.let {
                this.topic = TopicDao.find { TopicsTable.topicId eq it }.firstOrNull()
                    ?: throw IllegalArgumentException("Topic not found")
            }

            update.language?.let { this.language = it.name }
            update.difficulty?.let { this.difficulty = it.name }
            update.questionText?.let { this.questionText = it }

            update.propositions?.let { newPropositions ->
                val existingAnswers = existingQuestion.answers.associateBy { it.answer }

                val newAnswersMap = newPropositions.associateBy { it.text }

                existingAnswers.values.forEach { existingAnswer ->
                    if (!newAnswersMap.containsKey(existingAnswer.answer)) {
                        existingAnswer.delete()
                    }
                }

                newPropositions.forEach { newAnswer ->
                    val existingAnswer = existingAnswers[newAnswer.text]
                    if (existingAnswer != null) {
                        if (existingAnswer.isCorrect != newAnswer.isCorrect) {
                            existingAnswer.isCorrect = newAnswer.isCorrect
                        }
                    } else {
                        AnswerDao.new {
                            question = existingQuestion
                            answer = newAnswer.text
                            isCorrect = newAnswer.isCorrect
                        }
                    }
                }
            }
        }
    }

    suspend fun removeQuestion(questionId: String): Boolean = suspendTransaction {
        val rowsDeleted = TopicsTable.deleteWhere {
            QuestionsTable.id eq questionId
        }
        rowsDeleted == 1
    }

}