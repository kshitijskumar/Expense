package org.example.project.data.repository

import org.example.project.data.datasource.ExpenseLocalDataSource
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.repository.ExpenseRepository

class ExpenseRepositoryImpl(
    private val dataSource: ExpenseLocalDataSource
) : ExpenseRepository {
    
    override suspend fun addExpense(input: AddExpenseInput) {
        dataSource.insertWithParticipants(input)
    }
}
