package org.example.project.feature.addexpense.presentation

sealed interface AddExpenseIntent {
    data class TitleChanged(val title: String) : AddExpenseIntent
    data class AmountChanged(val amount: String) : AddExpenseIntent
    data class DateSelected(val date: Long) : AddExpenseIntent
    data class NotesChanged(val notes: String) : AddExpenseIntent
    data object DatePickerClicked : AddExpenseIntent
    data object DatePickerDismissed : AddExpenseIntent
    data object SaveClicked : AddExpenseIntent
    data object BackClicked : AddExpenseIntent
}
