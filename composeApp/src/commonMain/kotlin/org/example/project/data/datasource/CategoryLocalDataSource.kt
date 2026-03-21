package org.example.project.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.example.project.db.Category
import org.example.project.db.DatabaseHelper

class CategoryLocalDataSource(private val db: DatabaseHelper) {
    
    fun observeAllCategories(): Flow<List<Category>> {
        return db.categoryQueries.getAllCategories()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
    
    suspend fun insertCategory(name: String): Category = withContext(Dispatchers.IO) {
        db.categoryQueries.insertCategory(name)
        db.categoryQueries.lastInsertedCategory().executeAsOne()
    }
}
