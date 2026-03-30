package org.example.project.feature.monthlyreport.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.example.project.feature.monthlyreport.domain.MonthlyReportOrchestrator
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel
import org.example.project.util.DateTimeUtil
import org.example.project.util.getCurrentTimeMillis

class MonthlyReportViewModel(
    args: Screen.MonthlyReport,
    private val monthlyReportOrchestrator: MonthlyReportOrchestrator,
    private val navigationManager: NavigationManager
) : BaseViewModel<MonthlyReportState, MonthlyReportIntent>(MonthlyReportState()) {

    init {
        // Initialize with month and year from screen args using the intent handler
        // This ensures showMoveToCurrentMonth flag is computed correctly
        handleMonthChanged(args.month, args.year)

        // Subscribe to month changes and fetch analysis data
        // When selectedMonth or selectedYear changes, flatMapLatest cancels the previous subscription
        // and starts a new one with the new month/year
        state
            .map { it.selectedMonth to it.selectedYear }
            .distinctUntilChanged()
            .flatMapLatest { (month, year) ->
                // Only fetch data if both month and year are non-null
                if (month != null && year != null) {
                    monthlyReportOrchestrator.getMonthAnalysis(month, year)
                } else {
                    // This shouldn't happen in normal flow since we set month/year in init,
                    // but guard against it anyway
                    kotlinx.coroutines.flow.emptyFlow()
                }
            }
            .onEach { analysis ->
                updateState {
                    copy(
                        isLoading = false,
                        totalSpent = analysis.totalSpent,
                        allCategorySpendings = analysis.categorySpendings,
                        allFriendSpendings = analysis.friendSpendings,
                        transactions = analysis.transactions,
                        transactionsByDate = groupTransactionsByDate(analysis.transactions)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: MonthlyReportIntent) {
        when (intent) {
            MonthlyReportIntent.BackClicked -> navigationManager.navigateBack()
            is MonthlyReportIntent.MonthChanged -> handleMonthChanged(intent.month, intent.year)
            MonthlyReportIntent.PreviousMonthClicked -> handlePreviousMonthClicked()
            MonthlyReportIntent.NextMonthClicked -> handleNextMonthClicked()
            MonthlyReportIntent.MoveToCurrentMonthClicked -> handleMoveToCurrentMonthClicked()
            is MonthlyReportIntent.ViewAllCategoriesClicked -> handleViewAllCategoriesClicked()
            is MonthlyReportIntent.ViewAllFriendsClicked -> handleViewAllFriendsClicked()
            is MonthlyReportIntent.TransactionClicked -> handleTransactionClicked(intent.expenseId)
        }
    }

    private fun handleMonthChanged(month: Int, year: Int) {
        // Get current month/year to determine if selected month is different
        val (currentYear, currentMonth) = DateTimeUtil.getYearMonthFromTimestamp(DateTimeUtil.getCurrentTimeMillis())
        val isCurrentMonth = (month == currentMonth && year == currentYear)

        // Update selected month/year, which triggers flatMapLatest to fetch new data
        // Also compute showMoveToCurrentMonth flag based on whether selected month is current month
        // Note: no guard on future time because currently for adding flow also we dont have time check for future timings
        updateState {
            copy(
                selectedMonth = month,
                selectedYear = year,
                isLoading = true,
                showMoveToCurrentMonth = !isCurrentMonth
            )
        }
    }

    private fun handlePreviousMonthClicked() {
        // Get current month and year, calculate previous month
        val currentState = state.value
        if (currentState.selectedMonth != null && currentState.selectedYear != null) {
            val (prevMonth, prevYear) = DateTimeUtil.getPreviousMonth(
                currentState.selectedMonth,
                currentState.selectedYear
            )
            handleMonthChanged(prevMonth, prevYear)
        }
    }

    private fun handleNextMonthClicked() {
        // Get current month and year, calculate next month
        val currentState = state.value
        if (currentState.selectedMonth != null && currentState.selectedYear != null) {
            val (nextMonth, nextYear) = DateTimeUtil.getNextMonth(
                currentState.selectedMonth,
                currentState.selectedYear
            )
            handleMonthChanged(nextMonth, nextYear)
        }
    }

    private fun handleMoveToCurrentMonthClicked() {
        // Jump back to the current month
        val (currentYear, currentMonth) = DateTimeUtil.getYearMonthFromTimestamp(DateTimeUtil.getCurrentTimeMillis())
        handleMonthChanged(currentMonth, currentYear)
    }

    private fun handleViewAllCategoriesClicked() {
        val currentState = state.value
        if (currentState.selectedMonth != null && currentState.selectedYear != null) {
            navigationManager.navigateTo(
                Screen.CategorySpendAnalysis(
                    year = currentState.selectedYear,
                    month = currentState.selectedMonth
                )
            )
        }
    }

    private fun handleViewAllFriendsClicked() {
        // TODO: Implement navigation to view all friends
    }

    private fun handleTransactionClicked(expenseId: Long) {
        navigationManager.navigateTo(Screen.EditExpense(expenseId))
    }

    private fun groupTransactionsByDate(transactions: List<ExpenseDetailModel>): List<TransactionGroup> {
        return transactions
            .groupBy { DateTimeUtil.getLocalDateFromTimestamp(it.date) }
            .map { (date, txns) ->
                TransactionGroup(
                    dateLabel = DateTimeUtil.getRelativeDateLabel(date),
                    date = date,
                    transactions = txns.sortedByDescending { it.date }
                )
            }
            .sortedByDescending { it.date }
    }
}
