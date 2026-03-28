package org.example.project.feature.monthlyreport.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.feature.monthlyreport.domain.model.MonthAnalysisState

/**
 * Orchestrator for monthly report analysis.
 *
 * Provides reactive data for a given month/year:
 * - Total spent for the month
 * - All categories with their spending (sorted descending)
 * - All friends with their amount owed (sorted descending)
 * - All transactions for the month (sorted by date)
 *
 * This is a stateless, parameterized orchestrator - each call to getMonthAnalysis()
 * returns a new cold Flow independent of previous calls.
 *
 * The flow is fully reactive to changes in expense data:
 * - Adding/updating/deleting an expense automatically emits new data
 * - No manual refresh mechanism needed
 */
interface MonthlyReportOrchestrator {

    /**
     * Get monthly analysis data for a specific month/year.
     *
     * @param month 1-12 representing January-December
     * @param year The year (e.g., 2024)
     * @return A cold Flow<MonthAnalysisState> that emits whenever underlying expense data changes.
     *         Each call to this function creates a new, independent subscription.
     *
     * The returned flow contains:
     * - totalSpent: Sum of all expenses for the month
     * - categorySpendings: All categories with their total spending (sorted desc by amount)
     * - friendSpendings: All friends with total amount owed (sorted desc by amount owed)
     * - transactions: All expenses sorted by date (newest first)
     */
    fun getMonthAnalysis(month: Int, year: Int): Flow<MonthAnalysisState>
}
