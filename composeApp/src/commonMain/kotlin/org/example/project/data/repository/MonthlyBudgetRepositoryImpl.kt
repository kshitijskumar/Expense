package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.datasource.MonthlyBudgetLocalDataSource
import org.example.project.domain.repository.MonthlyBudgetRepository

class MonthlyBudgetRepositoryImpl(
    private val dataSource: MonthlyBudgetLocalDataSource
) : MonthlyBudgetRepository {
    
    override suspend fun getBudgetForMonth(month: String): Long? {
        return dataSource.getBudgetForMonth(month)
    }
    
    override fun getBudgetForMonthFlow(month: String): Flow<Long?> {
        return dataSource.getBudgetForMonthFlow(month)
    }
}
