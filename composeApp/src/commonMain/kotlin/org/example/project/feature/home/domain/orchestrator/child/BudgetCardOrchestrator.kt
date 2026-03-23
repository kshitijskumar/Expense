package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator

/**
 * Orchestrator for the budget card component.
 * Calculates monthly spend, budget, progress, and over-budget status.
 */
class BudgetCardOrchestrator : HomeComponentOrchestrator<HomeComponent.BudgetCard> {
    
    private val _componentState = MutableStateFlow<HomeComponent.BudgetCard?>(null)
    override val componentState: StateFlow<HomeComponent.BudgetCard?> = _componentState.asStateFlow()
    
    override suspend fun initialize() {
        // TODO: Replace with actual repository calls
        // Mock data: spent 5600 rupees (560000 paise) out of 20000 rupees (2000000 paise)
        val monthlySpend = 560000L // in paise
        val monthlyBudget = 2000000L // in paise

        val progress = calculateProgress(monthlySpend, monthlyBudget)
        val isOverBudget = monthlyBudget?.let { monthlySpend > it } ?: false

        _componentState.value = HomeComponent.BudgetCard(
            monthlySpend = monthlySpend,
            monthlyBudget = monthlyBudget,
            progress = progress,
            isOverBudget = isOverBudget
        )
    }
    
    private fun calculateProgress(spent: Long, budget: Long?): Float {
        return if (budget != null && budget > 0) {
            (spent.toFloat() / budget).coerceIn(0f, 1f)
        } else {
            1f // Show full circle when no budget set
        }
    }
}
