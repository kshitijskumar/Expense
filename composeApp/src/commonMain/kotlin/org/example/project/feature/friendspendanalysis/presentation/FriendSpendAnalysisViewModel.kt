package org.example.project.feature.friendspendanalysis.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.example.project.feature.friendspendanalysis.domain.FriendSpendAnalysisUseCase
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel

class FriendSpendAnalysisViewModel(
    args: Screen.FriendSpendAnalysis,
    private val friendSpendAnalysisUseCase: FriendSpendAnalysisUseCase,
    private val navigationManager: NavigationManager
) : BaseViewModel<FriendSpendAnalysisState, FriendSpendAnalysisIntent>(FriendSpendAnalysisState()) {

    init {
        onIntent(FriendSpendAnalysisIntent.Initialise(month = args.month, year = args.year))
    }

    override fun onIntent(intent: FriendSpendAnalysisIntent) {
        when (intent) {
            is FriendSpendAnalysisIntent.Initialise -> handleInitialise(intent.month, intent.year)
            FriendSpendAnalysisIntent.BackClicked -> handleBackClicked()
            is FriendSpendAnalysisIntent.FriendRowTapped -> handleFriendRowTapped(intent.friendId)
            is FriendSpendAnalysisIntent.ExpenseClicked -> handleExpenseClicked(intent.expenseId)
        }
    }

    private fun handleInitialise(month: Int, year: Int) {
        friendSpendAnalysisUseCase(month, year)
            .onEach { details ->
                updateState { copy(isLoading = false, friendSpendDetails = details) }
            }
            .launchIn(viewModelScope)
    }

    private fun handleBackClicked() {
        navigationManager.navigateBack()
    }

    private fun handleFriendRowTapped(friendId: Long) {
        updateState {
            val newExpandedId = if (expandedFriendId == friendId) null else friendId
            copy(expandedFriendId = newExpandedId)
        }
    }

    private fun handleExpenseClicked(expenseId: Long) {
        navigationManager.navigateTo(Screen.EditExpense(expenseId))
    }
}
