package com.soquiz.plugins

import com.soquiz.database.tables.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {

    val url = System.getenv("POSTGRES_URL")
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")
    val driver = environment.config.property("postgres.driver").getString()

    Database.connect(
        url = url,
        user = user,
        password = password,
        driver = driver
    )
}


fun Application.initializeDatabase() {
    transaction {
        SchemaUtils.create(
            UsersTable,
            CategoriesTable,
            TopicsTable,
            QuestionsTable,
            AnswersTable
        )
    }
}

