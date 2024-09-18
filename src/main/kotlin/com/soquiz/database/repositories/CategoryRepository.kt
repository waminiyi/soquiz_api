package com.soquiz.database.repositories


import com.soquiz.database.mapping.toCategory
import com.soquiz.database.tables.CategoriesTable
import com.soquiz.database.tables.CategoryDao
import com.soquiz.models.Category
import com.soquiz.utils.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class CategoryRepository {
    suspend fun allCategories(): List<Category> = suspendTransaction {
        CategoryDao.all().map { it.toCategory() }
    }


    suspend fun addCategory(category: Category) = suspendTransaction {
        CategoryDao.new {
            categoryId = category.categoryId
            defaultName = category.name
        }
    }

    suspend fun updateCategory(category: Category) = suspendTransaction {

        val categoryDao = CategoryDao.findById(category.categoryId)

        categoryDao?.apply {
            defaultName = category.name
        }

    }

    suspend fun removeCategory(categoryId: String): Boolean = suspendTransaction {
        val rowsDeleted = CategoriesTable.deleteWhere {
            CategoriesTable.categoryId eq categoryId
        }
        rowsDeleted == 1
    }

}