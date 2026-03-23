package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator
import org.example.project.util.DateTimeUtil

/**
 * Orchestrator for the transaction list component.
 * Reactively provides list of transactions for the latest transaction date,
 * or empty state if no transactions exist.
 */
class TransactionListOrchestrator(
    private val expenseRepository: ExpenseRepository
) : HomeComponentOrchestrator<HomeComponent> {
    
    private val _componentState = MutableStateFlow<HomeComponent?>(null)
    override val componentState: StateFlow<HomeComponent?> = _componentState.asStateFlow()
    
    override fun initialize(scope: CoroutineScope) {
        scope.launch {
            try {
                // Reactively observe latest transaction date
                expenseRepository.getLatestTransactionDateFlow()
                    .flatMapLatest { latestDate ->
                        if (latestDate == null) {
                            flowOf((latestDate to listOf()))
                        } else {
                            // Once we have a date, reactively observe transactions for that day
                            val startOfDay = DateTimeUtil.getStartOfDay(latestDate)
                            val endOfDay = DateTimeUtil.getEndOfDay(latestDate)

                            expenseRepository.getExpensesByDateFlow(startOfDay, endOfDay)
                                .map { (latestDate to it) }
                        }
                    }
                    .collect { (latestDate, transactions) ->
                        _componentState.value = if ( latestDate == null || transactions.isEmpty()) {
                            HomeComponent.EmptyTransactions
                        } else {
                            HomeComponent.TransactionList(
                                date = DateTimeUtil.formatDateHeader(latestDate),
                                transactions = transactions
                            )
                        }
                    }
            } catch (e: Exception) {
                _componentState.value = null
            }
        }
    }
}
