package com.soquiz.database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column


object CategoriesTable : IdTable<String>("categories") {
    val categoryId = varchar("id", 255).uniqueIndex()
    val defaultName = varchar("default_name", 255)

    override val id: Column<EntityID<String>> = categoryId.entityId()
}

class CategoryDao(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CategoryDao>(CategoriesTable)

    var categoryId by CategoriesTable.categoryId
    var defaultName by CategoriesTable.defaultName

}


