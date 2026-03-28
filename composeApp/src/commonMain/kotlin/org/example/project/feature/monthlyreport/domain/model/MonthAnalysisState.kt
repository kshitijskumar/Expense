package org.example.project.feature.monthlyreport.domain.model

import org.example.project.domain.model.ExpenseDetailModel

/**
 * Complete analysis data for a given month.
 *
 * @param totalSpent Sum of all expense amounts for the month
 * @param categorySpendings All categories involved in the month, sorted by total spending (descending).
 * @param friendSpendings All friends who participated in expenses, sorted by amount owed (descending).
 * @param transactions All expenses for the month, sorted by date (newest first).
 */
data class MonthAnalysisState(
    val totalSpent: Long,
    val categorySpendings: List<CategorySpend>,
    val friendSpendings: List<FriendSpend>,
    val transactions: List<ExpenseDetailModel>
)
