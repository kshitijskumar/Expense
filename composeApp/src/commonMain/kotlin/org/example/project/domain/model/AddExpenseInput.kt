package org.example.project.domain.model

data class AddExpenseInput(
    val title: String,
    val amount: Long,
    val date: Long,
    val category: CategoryModel,
    val notes: String?,
    val participantFriends: List<FriendModel>
)
