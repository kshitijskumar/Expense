package org.example.project.feature.home.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.feature.home.domain.orchestrator.HomeOrchestrator
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel
import org.example.project.util.DateTimeUtil

class HomeViewModel(
    private val homeOrchestrator: HomeOrchestrator,
    private val navigationManager: NavigationManager
) : BaseViewModel<HomeState, HomeIntent>(HomeState()) {

    init {
        homeOrchestrator.initialize(viewModelScope)
        viewModelScope.launch {
            homeOrchestrator.homeComponents.collect { components ->
                updateState {
                    copy(
                        isLoading = false,
                        components = components
                    )
                }
            }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> { /* Orchestrator refresh TBD; avoid duplicate initialize */ }
            HomeIntent.NavigateToAddExpense -> navigationManager.navigateTo(Screen.AddExpense)
            is HomeIntent.NavigateToExpenseDetail -> navigationManager.navigateTo(Screen.EditExpense(intent.expenseId))
            HomeIntent.NavigateToViewAllTransactions -> navigationManager.navigateTo(Screen.MonthlyReport(DateTimeUtil.getCurrentMonth()))
        }
    }
}
