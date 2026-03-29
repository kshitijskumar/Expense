package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.datasource.ExpenseLocalDataSource
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.model.ExpenseSummaryModel
import org.example.project.domain.repository.ExpenseRepository

class ExpenseRepositoryImpl(
    private val dataSource: ExpenseLocalDataSource
) : ExpenseRepository {

    override suspend fun addExpense(input: AddExpenseInput) {
        dataSource.insertWithParticipants(input)
    }

    override suspend fun getExpenseById(expenseId: Long): ExpenseDetailModel? {
        return dataSource.getExpenseById(expenseId)
    }

    override suspend fun updateExpense(expenseId: Long, input: AddExpenseInput) {
        dataSource.updateExpense(expenseId, input)
    }

    override suspend fun deleteExpense(expenseId: Long) {
        dataSource.deleteExpense(expenseId)
    }

    override suspend fun getLatestTransactionDate(): Long? {
        return dataSource.getLatestTransactionDate()
    }
    
    override suspend fun getExpensesByDate(startOfDay: Long, endOfDay: Long): List<ExpenseSummaryModel> {
        return dataSource.getExpensesByDate(startOfDay, endOfDay)
    }
    
    override suspend fun getTotalSpentForMonth(monthStart: Long, monthEnd: Long): Long {
        return dataSource.getTotalSpentForMonth(monthStart, monthEnd)
    }
    
    override fun getLatestTransactionDateFlow(): Flow<Long?> {
        return dataSource.getLatestTransactionDateFlow()
    }
    
    override fun getExpensesByDateFlow(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseSummaryModel>> {
        return dataSource.getExpensesByDateFlow(startOfDay, endOfDay)
    }
    
    override fun getTotalSpentForMonthFlow(monthStart: Long, monthEnd: Long): Flow<Long> {
        return dataSource.getTotalSpentForMonthFlow(monthStart, monthEnd)
    }

    override fun getExpensesWithParticipantsForMonthFlow(
        monthStart: Long,
        monthEnd: Long
    ): Flow<List<ExpenseDetailModel>> {
        return dataSource.getExpensesWithParticipantsForMonthFlow(monthStart, monthEnd)
    }
}
