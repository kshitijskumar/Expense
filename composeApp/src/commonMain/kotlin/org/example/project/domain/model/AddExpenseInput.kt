package org.example.project.domain.model

data class AddExpenseInput(
    val title: String,
    val amount: Long,
    val date: Long,
    val categoryId: Long,
    val notes: String?,
    val participantFriendIds: List<Long>
)
