package com.soquiz.database.mapping

import com.soquiz.database.tables.CategoryDao
import com.soquiz.models.Category

fun CategoryDao.toCategory() = Category(
    categoryId = this.categoryId,
    name = this.defaultName
)