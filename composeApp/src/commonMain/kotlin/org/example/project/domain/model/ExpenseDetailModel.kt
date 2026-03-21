package org.example.project.domain.model

data class ExpenseDetailModel(
    val id: Long,
    val title: String,
    val amount: Long,
    val date: Long,
    val category: CategoryModel,
    val participants: List<FriendModel>,
    val notes: String?
)
