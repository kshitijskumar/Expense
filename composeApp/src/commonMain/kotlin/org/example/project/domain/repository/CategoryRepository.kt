package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.CategoryModel

interface CategoryRepository {
    fun observeAllCategories(): Flow<List<CategoryModel>>
    suspend fun addCategory(name: String): CategoryModel
}
