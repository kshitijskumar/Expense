package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow

interface MonthlyBudgetRepository {
    suspend fun getBudgetForMonth(month: String): Long?
    
    // Reactive Flow-based method
    fun getBudgetForMonthFlow(month: String): Flow<Long?>
}
