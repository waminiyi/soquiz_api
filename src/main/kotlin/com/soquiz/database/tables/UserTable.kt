package com.soquiz.database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable("users") {
    val name = varchar("name", 50)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 64)
    val role = varchar("role", 10)

}

class UserDao(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, UserDao>(UsersTable)

    var name by UsersTable.name
    var email by UsersTable.email
    var password by UsersTable.password
    var role by UsersTable.role

}
