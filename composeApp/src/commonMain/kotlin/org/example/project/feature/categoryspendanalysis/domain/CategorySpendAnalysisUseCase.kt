package org.example.project.feature.categoryspendanalysis.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.CategorySpendDetail
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.util.DateTimeUtil

interface CategorySpendAnalysisUseCase {
    operator fun invoke(month: Int, year: Int): Flow<List<CategorySpendDetail>>
}

class CategorySpendAnalysisUseCaseImpl(
    private val expenseRepository: ExpenseRepository
) : CategorySpendAnalysisUseCase {
    override operator fun invoke(month: Int, year: Int): Flow<List<CategorySpendDetail>> {
        val (start, end) = DateTimeUtil.getMonthRange(month, year)
        return expenseRepository.getExpensesWithParticipantsForMonthFlow(start, end)
            .map { expenses ->
                expenses
                    .groupBy { it.category }
                    .map { (category, txns) ->
                        CategorySpendDetail(
                            category = category,
                            totalAmount = txns.sumOf { it.amount },
                            transactions = txns
                        )
                    }
                    .filter { it.totalAmount > 0 }
                    .sortedByDescending { it.totalAmount }
            }
    }
}
