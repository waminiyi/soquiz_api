package com.soquiz.database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object AnswersTable : IdTable<Int>("answers") {
    override val id: Column<EntityID<Int>> = integer("id").autoIncrement().entityId()

    val question = reference("question", QuestionsTable)
    val answer = varchar("answer", 255)
    val isCorrect = bool("is_correct")
    override val primaryKey = PrimaryKey(question, answer)
}

class AnswerDao(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AnswerDao>(AnswersTable)

    var question by QuestionDao referencedOn AnswersTable.question
    var answer by AnswersTable.answer
    var isCorrect by AnswersTable.isCorrect
}