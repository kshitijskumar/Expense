package org.example.project.feature.monthlyreport.domain

import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.feature.monthlyreport.domain.model.FriendSpend

/**
 * Computes how much each friend owes based on their participation in expenses.
 *
 * For equal split expenses:
 * - Each participant (including the user) owes: expense.amount / (participantCount + 1)
 * - The +1 accounts for the user who always participates in their own expenses
 *
 * Results are aggregated across all expenses and sorted by amount owed (descending).
 */
class ComputeFriendSpendsUseCase {

    operator fun invoke(expenses: List<ExpenseDetailModel>): List<FriendSpend> =
        expenses
            .flatMap { e ->
                e.participants.map { f ->
                    f to e.amount / (e.participants.size + 1)
                }
            }
            .groupBy({ it.first }, { it.second })
            .map { (friend, amounts) -> FriendSpend(friend, amounts.sum()) }
            .sortedByDescending { it.amountOwed }
}
