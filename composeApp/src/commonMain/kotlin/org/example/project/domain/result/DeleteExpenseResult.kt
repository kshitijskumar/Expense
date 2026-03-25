package org.example.project.domain.result

sealed class DeleteExpenseResult {
    data object Success : DeleteExpenseResult()
    data class Failure(val cause: Throwable) : DeleteExpenseResult()
}
