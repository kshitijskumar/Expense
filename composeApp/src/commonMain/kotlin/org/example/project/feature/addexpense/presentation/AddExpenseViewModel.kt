package org.example.project.feature.addexpense.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.result.AddExpenseResult
import org.example.project.feature.addexpense.domain.AddExpenseUseCase
import org.example.project.feature.category.CategorySelector
import org.example.project.feature.friend.FriendSelector
import org.example.project.navigation.NavigationManager
import org.example.project.ui.base.BaseViewModel

class AddExpenseViewModel(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val navigationManager: NavigationManager,
    private val categorySelector: CategorySelector,
    private val friendSelector: FriendSelector
) : BaseViewModel<AddExpenseState, AddExpenseIntent>(AddExpenseState()) {

    init {
        categorySelector.initialise(viewModelScope)
        friendSelector.initialise(viewModelScope)
        observeCategorySelection()
        observeFriendSelection()
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
            
            is AddExpenseIntent.CategorySearchQueryChanged -> {
                categorySelector.onSearchQueryChanged(intent.query)
            }
            is AddExpenseIntent.CategorySelected -> {
                categorySelector.onCategorySelected(intent.category)
            }
            AddExpenseIntent.CategoryAddNewClicked -> {
                viewModelScope.launch {
                    categorySelector.onAddNewCategory()
                }
            }
            AddExpenseIntent.CategorySearchCleared -> {
                categorySelector.onClearSearch()
            }
            
            is AddExpenseIntent.FriendSearchQueryChanged -> {
                friendSelector.onSearchQueryChanged(intent.query)
            }
            is AddExpenseIntent.FriendToggled -> {
                friendSelector.onFriendToggled(intent.friend)
            }
            AddExpenseIntent.FriendAddNewClicked -> {
                viewModelScope.launch {
                    friendSelector.onAddNewFriend()
                }
            }
            AddExpenseIntent.FriendSearchCleared -> {
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
}
