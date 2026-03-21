package org.example.project.domain.repository

import org.example.project.domain.model.AddExpenseInput

interface ExpenseRepository {
    suspend fun addExpense(input: AddExpenseInput): Long
}
