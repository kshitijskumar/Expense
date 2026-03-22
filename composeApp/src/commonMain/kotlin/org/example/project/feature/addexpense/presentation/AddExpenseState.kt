package org.example.project.feature.addexpense.presentation

import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.FriendModel
import org.example.project.domain.result.ExpenseValidationError
import org.example.project.feature.category.CategorySelectionState
import org.example.project.feature.friend.FriendSelectionState
import kotlin.time.Clock

data class AddExpenseState(
    val title: String = "",
    val amount: String = "",
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    val notes: String = "",
    val selectedCategory: CategoryModel? = null,
    val selectedFriends: List<FriendModel> = emptyList(),
    val isLoading: Boolean = false,
    val validationErrors: List<ExpenseValidationError> = emptyList(),
    val showDatePicker: Boolean = false,
    val categoryState: CategorySelectionState = CategorySelectionState(),
    val friendState: FriendSelectionState = FriendSelectionState()
)
