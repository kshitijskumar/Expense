package org.example.project.feature.addexpense.presentation

import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.model.FriendModel
import org.example.project.domain.result.ExpenseValidationError
import org.example.project.feature.category.CategorySelectionState
import org.example.project.feature.friend.FriendSelectionState
import kotlin.time.Clock

data class EditExpenseState(
    val expenseId: Long = 0L,
    val originalExpense: ExpenseDetailModel? = null,
    val title: String = "",
    val amount: String = "",
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    val notes: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val validationErrors: List<ExpenseValidationError> = emptyList(),
    val showDeleteConfirmation: Boolean = false,
    val showDatePicker: Boolean = false,
    val categoryState: CategorySelectionState = CategorySelectionState(),
    val friendState: FriendSelectionState = FriendSelectionState()
) {
    // Access category and friends from their respective selector states
    val selectedCategory: CategoryModel? get() = categoryState.selectedCategory
    val selectedFriends: List<FriendModel> get() = friendState.selectedFriends

    val enableSaveBtn: Boolean get() {
        return !isSaving && !isLoading && amount.isNotBlank() && selectedCategory != null && title.isNotBlank()
    }
}

sealed interface EditExpenseIntent {
    // Field changes
    data class TitleChanged(val title: String) : EditExpenseIntent
    data class AmountChanged(val amount: String) : EditExpenseIntent
    data class DateSelected(val date: Long) : EditExpenseIntent
    data class NotesChanged(val notes: String) : EditExpenseIntent

    // Date picker
    data object DatePickerClicked : EditExpenseIntent
    data object DatePickerDismissed : EditExpenseIntent

    // Category selection
    data class CategorySearchQueryChanged(val query: String) : EditExpenseIntent
    data class CategorySelected(val category: CategoryModel) : EditExpenseIntent
    data object CategoryAddNewClicked : EditExpenseIntent
    data object CategorySearchCleared : EditExpenseIntent

    // Friend selection
    data class FriendSearchQueryChanged(val query: String) : EditExpenseIntent
    data class FriendToggled(val friend: FriendModel) : EditExpenseIntent
    data object FriendAddNewClicked : EditExpenseIntent
    data object FriendSearchCleared : EditExpenseIntent

    // Save/Delete actions
    data object SaveClicked : EditExpenseIntent
    data object DeleteClicked : EditExpenseIntent
    data object ConfirmDeleteClicked : EditExpenseIntent
    data object CancelDeleteClicked : EditExpenseIntent
    data object BackClicked : EditExpenseIntent
}
