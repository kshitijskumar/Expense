@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.util.getCurrentTimeMillis

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
                            // Format date as "Today", "Yesterday", or "DD MMMM"
                            val dateLabel = formatTransactionDate(latestDate)
                            HomeComponent.TransactionList(
                                date = dateLabel,
                                transactions = transactions
                            )
                        }
                    }
            } catch (e: Exception) {
                _componentState.value = null
            }
        }
    }

    /**
     * Formats transaction date as "Today", "Yesterday", or "DD MMMM"
     */
    private fun formatTransactionDate(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val transactionDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        val nowInstant = Instant.fromEpochMilliseconds(DateTimeUtil.getCurrentTimeMillis())
        val todayDate = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when {
            transactionDate == todayDate -> "Today"
            transactionDate.dayOfYear == todayDate.dayOfYear - 1 && transactionDate.year == todayDate.year -> "Yesterday"
            else -> DateTimeUtil.formatDateHeader(timestamp)
        }
    }
}
