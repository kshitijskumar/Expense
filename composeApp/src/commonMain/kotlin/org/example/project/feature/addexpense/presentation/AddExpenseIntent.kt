package org.example.project.feature.addexpense.presentation

import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.FriendModel

sealed interface AddExpenseIntent {
    data class TitleChanged(val title: String) : AddExpenseIntent
    data class AmountChanged(val amount: String) : AddExpenseIntent
    data class DateSelected(val date: Long) : AddExpenseIntent
    data class NotesChanged(val notes: String) : AddExpenseIntent
    data object DatePickerClicked : AddExpenseIntent
    data object DatePickerDismissed : AddExpenseIntent
    data object SaveClicked : AddExpenseIntent
    data object BackClicked : AddExpenseIntent
    
    data class CategorySearchQueryChanged(val query: String) : AddExpenseIntent
    data class CategorySelected(val category: CategoryModel) : AddExpenseIntent
    data object CategoryAddNewClicked : AddExpenseIntent
    data object CategorySearchCleared : AddExpenseIntent
    
    data class FriendSearchQueryChanged(val query: String) : AddExpenseIntent
    data class FriendToggled(val friend: FriendModel) : AddExpenseIntent
    data object FriendAddNewClicked : AddExpenseIntent
    data object FriendSearchCleared : AddExpenseIntent
}
