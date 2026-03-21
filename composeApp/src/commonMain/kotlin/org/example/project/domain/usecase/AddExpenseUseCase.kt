package org.example.project.domain.usecase

import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.domain.result.AddExpenseResult
import org.example.project.domain.result.ExpenseValidationError

class AddExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(input: AddExpenseInput): AddExpenseResult {
        val validationErrors = validateInput(input)
        
        if (validationErrors.isNotEmpty()) {
            return AddExpenseResult.ValidationError(validationErrors)
        }
        
        return try {
            expenseRepository.addExpense(input)
            AddExpenseResult.Success
        } catch (e: Exception) {
            AddExpenseResult.Failure(e)
        }
    }
    
    private fun validateInput(input: AddExpenseInput): List<ExpenseValidationError> {
        val errors = mutableListOf<ExpenseValidationError>()
        
        if (input.title.isBlank()) {
            errors.add(ExpenseValidationError.EMPTY_TITLE)
        }
        
        if (input.amount <= 0) {
            errors.add(ExpenseValidationError.INVALID_AMOUNT)
        }
        
        return errors
    }
}
