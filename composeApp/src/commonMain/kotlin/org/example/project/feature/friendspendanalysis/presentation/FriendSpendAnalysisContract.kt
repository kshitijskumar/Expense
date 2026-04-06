package org.example.project.feature.friendspendanalysis.presentation

import org.example.project.domain.model.FriendSpendDetail

data class FriendSpendAnalysisState(
    val isLoading: Boolean = true,
    val friendSpendDetails: List<FriendSpendDetail> = emptyList(),
    val expandedFriendId: Long? = null
)

sealed interface FriendSpendAnalysisIntent {
    data class Initialise(val month: Int, val year: Int) : FriendSpendAnalysisIntent
    data object BackClicked : FriendSpendAnalysisIntent
    data class FriendRowTapped(val friendId: Long) : FriendSpendAnalysisIntent
    data class ExpenseClicked(val expenseId: Long) : FriendSpendAnalysisIntent
}
