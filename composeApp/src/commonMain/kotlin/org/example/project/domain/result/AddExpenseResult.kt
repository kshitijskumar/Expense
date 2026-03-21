package org.example.project.domain.result

sealed class AddExpenseResult {
    data object Success : AddExpenseResult()
    data class ValidationError(val errors: List<ExpenseValidationError>) : AddExpenseResult()
    data class Failure(val cause: Throwable) : AddExpenseResult()
}
