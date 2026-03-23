package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.ExpenseSummaryModel
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator

/**
 * Orchestrator for the transaction list component.
 * Provides list of transactions for the latest transaction date,
 * or empty state if no transactions exist.
 */
class TransactionListOrchestrator : HomeComponentOrchestrator<HomeComponent> {
    
    private val _componentState = MutableStateFlow<HomeComponent?>(null)
    override val componentState: StateFlow<HomeComponent?> = _componentState.asStateFlow()
    
    override suspend fun initialize() {
        // TODO: Replace with actual repository calls
        // Mock data: Create sample transactions
        val transactions = createMockTransactions()

        _componentState.value = if (transactions.isEmpty()) {
            HomeComponent.EmptyTransactions
        } else {
            HomeComponent.TransactionList(
                date = "23 March", // Mock date
                transactions = transactions
            )
        }
    }
    
    private fun createMockTransactions(): List<ExpenseSummaryModel> {
        // Mock: Create 2 sample transactions
        return listOf(
            ExpenseSummaryModel(
                id = 1L,
                title = "Blinkit",
                amount = 24300L, // Rs. 243 in paise
                date = System.currentTimeMillis(),
                category = CategoryModel(
                    id = 1L,
                    name = "Groceries",
                    budgetLimit = null
                ),
                participantCount = 2,
                notes = null
            ),
            ExpenseSummaryModel(
                id = 2L,
                title = "Lunch at Cafe",
                amount = 45000L, // Rs. 450 in paise
                date = System.currentTimeMillis(),
                category = CategoryModel(
                    id = 2L,
                    name = "Food",
                    budgetLimit = null
                ),
                participantCount = 3,
                notes = "Team lunch"
            )
        )
    }
}
