package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.model.ExpenseSummaryModel

interface ExpenseRepository {
    suspend fun addExpense(input: AddExpenseInput)

    suspend fun getExpenseById(expenseId: Long): ExpenseDetailModel?

    suspend fun updateExpense(expenseId: Long, input: AddExpenseInput)

    suspend fun deleteExpense(expenseId: Long)

    suspend fun getLatestTransactionDate(): Long?

    suspend fun getExpensesByDate(startOfDay: Long, endOfDay: Long): List<ExpenseSummaryModel>

    suspend fun getTotalSpentForMonth(monthStart: Long, monthEnd: Long): Long

    // Reactive Flow-based methods
    fun getLatestTransactionDateFlow(): Flow<Long?>

    fun getExpensesByDateFlow(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseSummaryModel>>

    fun getTotalSpentForMonthFlow(monthStart: Long, monthEnd: Long): Flow<Long>
}
