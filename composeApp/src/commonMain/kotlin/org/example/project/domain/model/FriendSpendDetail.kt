package org.example.project.domain.model

data class FriendSpendDetail(
    val friend: FriendModel,
    val totalAmountOwed: Long,
    val transactions: List<ExpenseDetailModel>
)
