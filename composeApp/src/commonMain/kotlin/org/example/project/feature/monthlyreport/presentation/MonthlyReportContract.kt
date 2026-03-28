package org.example.project.feature.monthlyreport.presentation

import org.example.project.feature.monthlyreport.domain.model.CategorySpend
import org.example.project.feature.monthlyreport.domain.model.FriendSpend
import org.example.project.domain.model.ExpenseDetailModel

/**
 * UI state for MonthlyReport screen.
 *
 * @param selectedMonth 1-12 for the currently selected month. Null during initialization
 *                      until constructor args are processed.
 * @param selectedYear The currently selected year. Null during initialization until constructor args are processed.
 * @param isLoading True while orchestrator is computing analysis data
 * @param totalSpent Total amount spent in the selected month. Null until data loads.
 * @param allCategorySpendings All categories for the month (sorted desc by amount).
 *                             UI shows top 3, with option to view all.
 *                             Empty list if no data loaded yet.
 * @param allFriendSpendings All friends for the month (sorted desc by amount owed).
 *                           UI shows top 3, with option to view all.
 *                           Empty list if no data loaded yet.
 * @param transactions All expenses for the month (sorted desc by date).
 *                     UI will group these by date for display.
 *                     Empty list if no data loaded yet.
 */
data class MonthlyReportState(
    val selectedMonth: Int? = null,
    val selectedYear: Int? = null,
    val isLoading: Boolean = true,
    val totalSpent: Long? = null,
    val allCategorySpendings: List<CategorySpend> = emptyList(),
    val allFriendSpendings: List<FriendSpend> = emptyList(),
    val transactions: List<ExpenseDetailModel> = emptyList()
) {
    /**
     * Top 3 categories by spending. Computed from allCategorySpendings.
     * UI uses this for the summary card; "View all" shows allCategorySpendings.
     */
    val topCategories: List<CategorySpend> get() = allCategorySpendings.take(3)

    /**
     * Top 3 friends by amount owed. Computed from allFriendSpendings.
     * UI uses this for the summary card; "View all" shows allFriendSpendings.
     */
    val topFriends: List<FriendSpend> get() = allFriendSpendings.take(3)
}

/**
 * User intents for the MonthlyReport screen.
 */
sealed interface MonthlyReportIntent {
    /**
     * User manually selected a new month/year from picker or navigation.
     * Triggers orchestrator subscription for the new month.
     */
    data class MonthChanged(val month: Int, val year: Int) : MonthlyReportIntent

    /**
     * Navigate to previous month.
     */
    data object PreviousMonthClicked : MonthlyReportIntent

    /**
     * Navigate to next month.
     */
    data object NextMonthClicked : MonthlyReportIntent

    /**
     * User clicked "View all categories" button.
     * Navigate to a detail screen showing all categories for the month.
     */
    data class ViewAllCategoriesClicked(val categories: List<CategorySpend>) : MonthlyReportIntent

    /**
     * User clicked "View all friends" button.
     * Navigate to a detail screen showing all friends and amounts owed for the month.
     */
    data class ViewAllFriendsClicked(val friends: List<FriendSpend>) : MonthlyReportIntent

    /**
     * User tapped on a transaction to view its details.
     */
    data class TransactionClicked(val expenseId: Long) : MonthlyReportIntent

    /**
     * User navigated back from the screen.
     */
    data object BackClicked : MonthlyReportIntent
}
