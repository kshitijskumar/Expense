package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.domain.repository.MonthlyBudgetRepository
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator
import org.example.project.util.DateTimeUtil

/**
 * Orchestrator for the budget card component.
 * Reactively calculates monthly spend, budget, progress, and over-budget status.
 */
class BudgetCardOrchestrator(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: MonthlyBudgetRepository
) : HomeComponentOrchestrator<HomeComponent.BudgetCard> {
    
    private val _componentState = MutableStateFlow<HomeComponent.BudgetCard?>(null)
    override val componentState: StateFlow<HomeComponent.BudgetCard?> = _componentState.asStateFlow()
    
    override fun initialize(scope: CoroutineScope) {
        val currentMonth = DateTimeUtil.getCurrentMonth()
        val monthStart = DateTimeUtil.getMonthStartTimestamp(currentMonth)
        val monthEnd = DateTimeUtil.getMonthEndTimestamp(currentMonth)
        
        scope.launch {
            try {
                combine(
                    expenseRepository.getTotalSpentForMonthFlow(monthStart, monthEnd),
                    budgetRepository.getBudgetForMonthFlow(currentMonth)
                ) { monthlySpend, monthlyBudget ->
                    // Skip if no budget is set
                    if (monthlyBudget == null) {
                        HomeComponent.BudgetCard(
                            monthlySpend = monthlySpend,
                            monthlyBudget = monthlyBudget,
                            progress = 100f,
                            isOverBudget = false
                        )
                    } else {
                        val progress = calculateProgress(monthlySpend, monthlyBudget)
                        val isOverBudget = monthlySpend > monthlyBudget
                        
                        HomeComponent.BudgetCard(
                            monthlySpend = monthlySpend,
                            monthlyBudget = monthlyBudget,
                            progress = progress,
                            isOverBudget = isOverBudget
                        )
                    }
                }.collect { budgetCard ->
                    _componentState.value = budgetCard
                }
            } catch (e: Exception) {
                _componentState.value = null
            }
        }
    }
    
    private fun calculateProgress(spent: Long, budget: Long): Float {
        return if (budget > 0) {
            (spent.toFloat() / budget).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
}
