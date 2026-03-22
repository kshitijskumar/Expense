package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.datasource.CategoryLocalDataSource
import org.example.project.db.Category
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val dataSource: CategoryLocalDataSource
) : CategoryRepository {
    
    override fun observeAllCategories(): Flow<List<CategoryModel>> {
        return dataSource.observeAllCategories()
            .map { categories -> categories.map { it.toCategoryModel() } }
    }
    
    override suspend fun addCategory(name: String): CategoryModel {
        val category = dataSource.insertCategory(name)
        return category.toCategoryModel()
    }
    
    private fun Category.toCategoryModel() = CategoryModel(
        id = id,
        name = name
    )
}
