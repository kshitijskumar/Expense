package org.example.project.feature.home.domain.model

import org.example.project.domain.model.ExpenseSummaryModel

/**
 * Sealed interface representing different UI components on the home screen.
 * Each component type carries its own data required for rendering.
 */
sealed interface HomeComponent {
    
    /**
     * Date header showing the current date (e.g., "23 March")
     */
    data class DateHeader(val formattedDate: String) : HomeComponent
    
    /**
     * Budget card showing monthly spend, budget, and progress indicator
     */
    data class BudgetCard(
        val monthlySpend: Long,
        val monthlyBudget: Long?,
        val progress: Float,
        val isOverBudget: Boolean
    ) : HomeComponent
    
    /**
     * List of transactions for a specific date
     */
    data class TransactionList(
        val date: String,
        val transactions: List<ExpenseSummaryModel>
    ) : HomeComponent
    
    /**
     * Empty state when there are no transactions
     */
    data object EmptyTransactions : HomeComponent
    
    /**
     * Error card shown when a component fails to load
     */
    data class ErrorCard(
        val componentType: String,
        val message: String
    ) : HomeComponent
}
