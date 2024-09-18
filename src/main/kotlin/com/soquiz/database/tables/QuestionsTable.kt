package com.soquiz.database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object QuestionsTable : IdTable<String>("questions") {
    override val id: Column<EntityID<String>> = varchar("id", 255).uniqueIndex().entityId()

    val category = reference("category", CategoriesTable)
    val topic = reference("topic", TopicsTable)
    val language = varchar("language", 5)
    val difficulty = varchar("difficulty", 20)
    val questionText = varchar("question_text", 255)
}

class QuestionDao(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, QuestionDao>(QuestionsTable)

    var category by CategoryDao referencedOn QuestionsTable.category
    var topic by TopicDao referencedOn QuestionsTable.topic
    var language by QuestionsTable.language
    var difficulty by QuestionsTable.difficulty
    var questionText by QuestionsTable.questionText

    val answers by AnswerDao referrersOn AnswersTable.question

}