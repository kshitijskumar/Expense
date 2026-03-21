package org.example.project.domain.repository

import org.example.project.domain.model.CategoryModel

interface CategoryRepository {
    suspend fun getAllCategories(): List<CategoryModel>
}
