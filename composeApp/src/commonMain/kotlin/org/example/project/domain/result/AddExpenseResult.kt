package org.example.project.domain.result

sealed class AddExpenseResult {
    data class Success(val expenseId: Long) : AddExpenseResult()
    data class ValidationError(val errors: List<ExpenseValidationError>) : AddExpenseResult()
    data class Failure(val cause: Throwable) : AddExpenseResult()
}
