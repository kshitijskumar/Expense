package org.example.project.feature.monthlyreport.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.example.project.feature.monthlyreport.domain.MonthlyReportOrchestrator
import org.example.project.navigation.NavigationManager
import org.example.project.ui.base.BaseViewModel
import org.example.project.util.DateTimeUtil
import org.example.project.util.getCurrentTimeMillis

class MonthlyReportViewModel(
    private val monthlyReportOrchestrator: MonthlyReportOrchestrator,
    private val navigationManager: NavigationManager
) : BaseViewModel<MonthlyReportState, MonthlyReportIntent>(MonthlyReportState()) {

    init {
        // Initialize with current month/year
        val (year, month) = DateTimeUtil.getYearMonthFromTimestamp(getCurrentTimeMillis())
        updateState {
            copy(selectedMonth = month, selectedYear = year)
        }

        // Subscribe to month changes and fetch analysis data
        // When selectedMonth or selectedYear changes, flatMapLatest cancels the previous subscription
        // and starts a new one with the new month/year
        state
            .map { it.selectedMonth to it.selectedYear }
            .distinctUntilChanged()
            .flatMapLatest { (month, year) ->
                monthlyReportOrchestrator.getMonthAnalysis(month, year)
            }
            .onEach { analysis ->
                updateState {
                    copy(
                        isLoading = false,
                        totalSpent = analysis.totalSpent,
                        allCategorySpendings = analysis.categorySpendings,
                        allFriendSpendings = analysis.friendSpendings,
                        transactions = analysis.transactions
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: MonthlyReportIntent) {
        when (intent) {
            is MonthlyReportIntent.MonthChanged -> handleMonthChanged(intent.month, intent.year)
            MonthlyReportIntent.PreviousMonthClicked -> handlePreviousMonthClicked()
            MonthlyReportIntent.NextMonthClicked -> handleNextMonthClicked()
            is MonthlyReportIntent.ViewAllCategoriesClicked -> handleViewAllCategoriesClicked()
            is MonthlyReportIntent.ViewAllFriendsClicked -> handleViewAllFriendsClicked()
            is MonthlyReportIntent.TransactionClicked -> handleTransactionClicked(intent.expenseId)
            MonthlyReportIntent.BackClicked -> handleBackClicked()
        }
    }

    private fun handleMonthChanged(month: Int, year: Int) {
        // TODO: Implement month change
    }

    private fun handlePreviousMonthClicked() {
        // TODO: Implement previous month navigation
    }

    private fun handleNextMonthClicked() {
        // TODO: Implement next month navigation
    }

    private fun handleViewAllCategoriesClicked() {
        // TODO: Implement navigation to view all categories
    }

    private fun handleViewAllFriendsClicked() {
        // TODO: Implement navigation to view all friends
    }

    private fun handleTransactionClicked(expenseId: Long) {
        // TODO: Implement transaction detail navigation
    }

    private fun handleBackClicked() {
        // TODO: Implement back navigation
    }
}
