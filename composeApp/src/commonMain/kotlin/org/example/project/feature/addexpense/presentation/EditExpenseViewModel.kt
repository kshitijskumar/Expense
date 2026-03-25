package org.example.project.feature.addexpense.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.result.AddExpenseResult
import org.example.project.domain.result.DeleteExpenseResult
import org.example.project.feature.addexpense.domain.DeleteExpenseUseCase
import org.example.project.feature.addexpense.domain.GetExpenseUseCase
import org.example.project.feature.addexpense.domain.GetExpenseResult
import org.example.project.feature.addexpense.domain.UpdateExpenseUseCase
import org.example.project.feature.category.CategorySelector
import org.example.project.feature.friend.FriendSelector
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel

class EditExpenseViewModel(
    val expenseId: Long,
    private val getExpenseUseCase: GetExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val navigationManager: NavigationManager,
    private val categorySelector: CategorySelector,
    private val friendSelector: FriendSelector
) : BaseViewModel<EditExpenseState, EditExpenseIntent>(EditExpenseState(expenseId = expenseId)) {

    init {
        categorySelector.initialise(viewModelScope)
        friendSelector.initialise(viewModelScope)
        observeCategorySelection()
        observeFriendSelection()
        loadExpense()
    }

    private fun loadExpense() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val result = getExpenseUseCase(expenseId)

            when (result) {
                is GetExpenseResult.Success -> {
                    val expense = result.expense
                    updateState {
                        copy(
                            originalExpense = expense,
                            title = expense.title,
                            amount = convertFromSmallestUnit(expense.amount),
                            date = expense.date,
                            notes = expense.notes ?: "",
                            selectedCategory = expense.category,
                            selectedFriends = expense.participants,
                            isLoading = false
                        )
                    }
                    // Update selectors with loaded data
                    categorySelector.onCategorySelected(expense.category)
                    expense.participants.forEach { friend ->
                        friendSelector.onFriendToggled(friend)
                    }
                }
                is GetExpenseResult.NotFound -> {
                    updateState { copy(isLoading = false) }
                    navigationManager.navigateBack()
                }
                is GetExpenseResult.Failure -> {
                    updateState { copy(isLoading = false) }
                    // TODO: Show error toast/snackbar
                }
            }
        }
    }

    private fun observeCategorySelection() {
        viewModelScope.launch {
            categorySelector.state.collect { selectorState ->
                updateState {
                    copy(
                        selectedCategory = selectorState.selectedCategory,
                        categoryState = selectorState
                    )
                }
            }
        }
    }

    private fun observeFriendSelection() {
        viewModelScope.launch {
            friendSelector.state.collect { selectorState ->
                updateState {
                    copy(
                        selectedFriends = selectorState.selectedFriends,
                        friendState = selectorState
                    )
                }
            }
        }
    }

    override fun onIntent(intent: EditExpenseIntent) {
        when (intent) {
            is EditExpenseIntent.TitleChanged -> updateState { copy(title = intent.title) }
            is EditExpenseIntent.AmountChanged -> handleAmountChanged(intent.amount)
            is EditExpenseIntent.DateSelected -> updateState {
                copy(date = intent.date, showDatePicker = false)
            }
            is EditExpenseIntent.NotesChanged -> updateState { copy(notes = intent.notes) }
            EditExpenseIntent.DatePickerClicked -> updateState { copy(showDatePicker = true) }
            EditExpenseIntent.DatePickerDismissed -> updateState { copy(showDatePicker = false) }
            EditExpenseIntent.SaveClicked -> handleSave()
            EditExpenseIntent.DeleteClicked -> updateState { copy(showDeleteConfirmation = true) }
            EditExpenseIntent.ConfirmDeleteClicked -> handleDelete()
            EditExpenseIntent.CancelDeleteClicked -> updateState { copy(showDeleteConfirmation = false) }
            EditExpenseIntent.BackClicked -> navigationManager.navigateBack()

            is EditExpenseIntent.CategorySearchQueryChanged -> {
                categorySelector.onSearchQueryChanged(intent.query)
            }
            is EditExpenseIntent.CategorySelected -> {
                categorySelector.onCategorySelected(intent.category)
            }
            EditExpenseIntent.CategoryAddNewClicked -> {
                viewModelScope.launch {
                    categorySelector.onAddNewCategory()
                }
            }
            EditExpenseIntent.CategorySearchCleared -> {
                categorySelector.onClearSearch()
            }

            is EditExpenseIntent.FriendSearchQueryChanged -> {
                friendSelector.onSearchQueryChanged(intent.query)
            }
            is EditExpenseIntent.FriendToggled -> {
                friendSelector.onFriendToggled(intent.friend)
            }
            EditExpenseIntent.FriendAddNewClicked -> {
                viewModelScope.launch {
                    friendSelector.onAddNewFriend()
                }
            }
            EditExpenseIntent.FriendSearchCleared -> {
                friendSelector.onClearSearch()
            }
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
            updateState { copy(isSaving = true, validationErrors = emptyList()) }

            val input = mapToAddExpenseInput()
            val result = updateExpenseUseCase(expenseId, input)

            when (result) {
                is AddExpenseResult.Success -> {
                    navigationManager.navigateBack()
                }
                is AddExpenseResult.ValidationError -> {
                    updateState {
                        copy(
                            isSaving = false,
                            validationErrors = result.errors
                        )
                    }
                }
                is AddExpenseResult.Failure -> {
                    updateState { copy(isSaving = false) }
                    // TODO: Show error toast/snackbar
                }
            }
        }
    }

    private fun handleDelete() {
        viewModelScope.launch {
            updateState { copy(isDeleting = true, showDeleteConfirmation = false) }

            val result = deleteExpenseUseCase(expenseId)

            when (result) {
                is DeleteExpenseResult.Success -> {
                    navigationManager.navigateBackTo(Screen.Home)
                }
                is DeleteExpenseResult.Failure -> {
                    updateState { copy(isDeleting = false) }
                    // TODO: Show error toast/snackbar
                }
            }
        }
    }

    private fun mapToAddExpenseInput(): AddExpenseInput {
        val selectedCategory = state.value.selectedCategory
            ?: throw IllegalStateException("Category must be selected")

        return AddExpenseInput(
            title = state.value.title,
            amount = convertToSmallestUnit(state.value.amount),
            date = state.value.date,
            category = selectedCategory,
            notes = state.value.notes.takeIf { it.isNotBlank() },
            participantFriends = state.value.selectedFriends
        )
    }

    private fun convertToSmallestUnit(amountString: String): Long {
        if (amountString.isBlank()) return 0L

        val amountDouble = amountString.toDoubleOrNull() ?: 0.0
        // Multiply by 100 to convert rupees to paise (or dollars to cents)
        return (amountDouble * 100).toLong()
    }

    private fun convertFromSmallestUnit(amount: Long): String {
        // Convert from paise/cents to rupees/dollars
        // e.g., 12550 paise -> "125.50"
        val amountDouble = amount / 100.0
        return if (amountDouble == amountDouble.toLong().toDouble()) {
            amountDouble.toLong().toString()
        } else {
            String.format("%.2f", amountDouble).trimEnd('0').trimEnd('.')
        }
    }
}
