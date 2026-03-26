package org.example.project.feature.addexpense.domain

import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.domain.result.AddExpenseResult

class UpdateExpenseUseCase(
    private val expenseRepository: ExpenseRepository,
    private val validation: ExpenseInputValidation
) {
    suspend operator fun invoke(expenseId: Long, input: AddExpenseInput): AddExpenseResult {
        val validationErrors = validation.validateInput(input)

        if (validationErrors.isNotEmpty()) {
            return AddExpenseResult.ValidationError(validationErrors)
        }

        return try {
            expenseRepository.updateExpense(expenseId, input)
            AddExpenseResult.Success
        } catch (e: Exception) {
            AddExpenseResult.Failure(e)
        }
    }
}
