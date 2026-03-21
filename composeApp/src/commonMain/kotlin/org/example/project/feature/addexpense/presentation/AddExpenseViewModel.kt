package org.example.project.feature.addexpense.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.result.AddExpenseResult
import org.example.project.feature.addexpense.domain.AddExpenseUseCase
import org.example.project.navigation.NavigationManager
import org.example.project.ui.base.BaseViewModel

class AddExpenseViewModel(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val navigationManager: NavigationManager
) : BaseViewModel<AddExpenseState, AddExpenseIntent>(AddExpenseState()) {

    override fun onIntent(intent: AddExpenseIntent) {
        when (intent) {
            is AddExpenseIntent.TitleChanged -> updateState { copy(title = intent.title) }
            is AddExpenseIntent.AmountChanged -> handleAmountChanged(intent.amount)
            is AddExpenseIntent.DateSelected -> updateState { 
                copy(date = intent.date, showDatePicker = false) 
            }
            is AddExpenseIntent.NotesChanged -> updateState { copy(notes = intent.notes) }
            AddExpenseIntent.DatePickerClicked -> updateState { copy(showDatePicker = true) }
            AddExpenseIntent.DatePickerDismissed -> updateState { copy(showDatePicker = false) }
            AddExpenseIntent.SaveClicked -> handleSave()
            AddExpenseIntent.BackClicked -> navigationManager.navigateBack()
        }
    }

    private fun handleAmountChanged(amount: String) {
        // Allow empty string (for clearing input) or valid decimal numbers
        // Valid formats: "125", "125.", "125.5", "125.50"
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            updateState { copy(amount = amount) }
        }
    }

    private fun handleSave() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, validationErrors = emptyList()) }

            val input = mapToAddExpenseInput()
            val result = addExpenseUseCase(input)

            when (result) {
                is AddExpenseResult.Success -> {
                    navigationManager.navigateBack()
                }
                is AddExpenseResult.ValidationError -> {
                    updateState {
                        copy(
                            isLoading = false,
                            validationErrors = result.errors
                        )
                    }
                }
                is AddExpenseResult.Failure -> {
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }

    private fun mapToAddExpenseInput(): AddExpenseInput {
        return AddExpenseInput(
            title = state.value.title,
            amount = convertToSmallestUnit(state.value.amount),
            date = state.value.date,
            categoryId = 1L,
            notes = state.value.notes.takeIf { it.isNotBlank() },
            participantFriendIds = emptyList()
        )
    }

    private fun convertToSmallestUnit(amountString: String): Long {
        if (amountString.isBlank()) return 0L
        
        val amountDouble = amountString.toDoubleOrNull() ?: 0.0
        // Multiply by 100 to convert rupees to paise (or dollars to cents)
        return (amountDouble * 100).toLong()
    }
}
