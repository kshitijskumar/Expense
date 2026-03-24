package org.example.project.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.db.DatabaseHelper

class MonthlyBudgetLocalDataSource(private val db: DatabaseHelper) {
    
    suspend fun getBudgetForMonth(month: String): Long? = withContext(Dispatchers.IO) {
        db.monthlyBudgetQueries.getBudgetForMonth(month)
            .executeAsOneOrNull()
            ?.total_limit
    }
    
    fun getBudgetForMonthFlow(month: String): Flow<Long?> {
        return db.monthlyBudgetQueries.getBudgetForMonth(month)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.total_limit }
    }
}
