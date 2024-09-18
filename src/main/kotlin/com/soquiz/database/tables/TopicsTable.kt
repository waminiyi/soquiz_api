package com.soquiz.database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object TopicsTable : IdTable<String>("topics") {
    val topicId = varchar("id", 255).uniqueIndex()
    val category = reference("category", CategoriesTable, ReferenceOption.CASCADE)
    val defaultName = varchar("default_name", 255)
    override val id: Column<EntityID<String>> = topicId.entityId()
}

class TopicDao(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TopicDao>(TopicsTable)

    var topicId by TopicsTable.topicId
    var category by CategoryDao referencedOn TopicsTable.category
    var defaultName by TopicsTable.defaultName

}