package org.example.project.feature.monthlyreport.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.feature.monthlyreport.domain.model.CategorySpend
import org.example.project.feature.monthlyreport.domain.model.MonthAnalysisState
import org.example.project.util.DateTimeUtil

class MonthlyReportOrchestratorImpl(
    private val expenseRepository: ExpenseRepository,
    private val computeFriendSpendsUseCase: ComputeFriendSpendsUseCase
) : MonthlyReportOrchestrator {

    override fun getMonthAnalysis(month: Int, year: Int): Flow<MonthAnalysisState> {
        val (start, end) = DateTimeUtil.getMonthRange(month, year)
        return expenseRepository.getExpensesWithParticipantsForMonthFlow(start, end)
            .map { expenses ->
                MonthAnalysisState(
                    totalSpent = expenses.sumOf { it.amount },
                    categorySpendings = computeCategorySpendings(expenses),
                    friendSpendings = computeFriendSpendsUseCase(expenses),
                    transactions = expenses
                )
            }
    }

    private fun computeCategorySpendings(expenses: List<ExpenseDetailModel>): List<CategorySpend> =
        expenses
            .groupBy { it.category }
            .map { (category, exps) -> CategorySpend(category, exps.sumOf { it.amount }) }
            .sortedByDescending { it.totalAmount }
}
