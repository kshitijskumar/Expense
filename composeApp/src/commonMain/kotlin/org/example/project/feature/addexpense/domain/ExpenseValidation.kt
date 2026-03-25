package org.example.project.feature.addexpense.domain

import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.result.ExpenseValidationError

/**
 * Interface for input validation logic.
 * Provides a centralized place for all expense input validations.
 * New independent field validations can be added as separate methods.
 */
interface ExpenseInputValidation {
    fun validateInput(input: AddExpenseInput): List<ExpenseValidationError>
}

/**
 * Implementation of expense input validation.
 * Contains all validation logic for expense fields.
 */
class ExpenseInputValidationImpl : ExpenseInputValidation {
    override fun validateInput(input: AddExpenseInput): List<ExpenseValidationError> {
        val errors = mutableListOf<ExpenseValidationError>()
        errors.addAll(validateTitle(input.title))
        errors.addAll(validateAmount(input.amount))
        return errors
    }

    private fun validateTitle(title: String): List<ExpenseValidationError> {
        return if (title.isBlank()) {
            listOf(ExpenseValidationError.EMPTY_TITLE)
        } else {
            emptyList()
        }
    }

    private fun validateAmount(amount: Long): List<ExpenseValidationError> {
        return if (amount <= 0) {
            listOf(ExpenseValidationError.INVALID_AMOUNT)
        } else {
            emptyList()
        }
    }
}
