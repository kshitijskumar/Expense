package org.example.project.feature.addexpense.domain

import org.example.project.domain.repository.ExpenseRepository
import org.example.project.domain.result.DeleteExpenseResult

class DeleteExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: Long): DeleteExpenseResult {
        return try {
            expenseRepository.deleteExpense(expenseId)
            DeleteExpenseResult.Success
        } catch (e: Exception) {
            DeleteExpenseResult.Failure(e)
        }
    }
}
