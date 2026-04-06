package org.example.project.feature.friendspendanalysis.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.FriendSpendDetail
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.util.DateTimeUtil

interface FriendSpendAnalysisUseCase {
    operator fun invoke(month: Int, year: Int): Flow<List<FriendSpendDetail>>
}

class FriendSpendAnalysisUseCaseImpl(
    private val expenseRepository: ExpenseRepository
) : FriendSpendAnalysisUseCase {
    override operator fun invoke(month: Int, year: Int): Flow<List<FriendSpendDetail>> {
        val (start, end) = DateTimeUtil.getMonthRange(month, year)
        return expenseRepository.getExpensesWithParticipantsForMonthFlow(start, end)
            .map { expenses ->
                expenses
                    .filter { it.participants.isNotEmpty() }
                    .flatMap { expense ->
                        expense.participants.map { friend -> friend to expense }
                    }
                    .groupBy({ it.first }, { it.second })
                    .map { (friend, txns) ->
                        val totalOwed = txns.sumOf { it.amount / (it.participants.size + 1) }
                        FriendSpendDetail(
                            friend = friend,
                            totalAmountOwed = totalOwed,
                            transactions = txns
                        )
                    }
                    .filter { it.totalAmountOwed > 0 }
                    .sortedByDescending { it.totalAmountOwed }
            }
    }
}
