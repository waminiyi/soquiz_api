package com.soquiz.database.tables

import com.soquizz.auth.hashPassword
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

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


fun populateAdminUser() {
    transaction {
        if (UsersTable.select { UsersTable.email eq "admin@domain.com" }.count() == 0L) {
            UsersTable.insert {
                it[name] = "Admin"
                it[email] = "admin@domain.com"
                it[password] = hashPassword("adminPassword")
                it[role] = "admin"
            }
        }
    }
}
