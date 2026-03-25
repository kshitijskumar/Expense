package org.example.project.feature.addexpense.domain

import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.repository.ExpenseRepository

sealed class GetExpenseResult {
    data class Success(val expense: ExpenseDetailModel) : GetExpenseResult()
    data object NotFound : GetExpenseResult()
    data class Failure(val cause: Throwable) : GetExpenseResult()
}

class GetExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: Long): GetExpenseResult {
        return try {
            val expense = expenseRepository.getExpenseById(expenseId)
            if (expense != null) {
                GetExpenseResult.Success(expense)
            } else {
                GetExpenseResult.NotFound
            }
        } catch (e: Exception) {
            GetExpenseResult.Failure(e)
        }
    }
}
