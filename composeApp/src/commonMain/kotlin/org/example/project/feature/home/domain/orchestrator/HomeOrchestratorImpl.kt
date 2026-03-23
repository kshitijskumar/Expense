package org.example.project.feature.home.domain.orchestrator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.child.BudgetCardOrchestrator
import org.example.project.feature.home.domain.orchestrator.child.DateHeaderOrchestrator
import org.example.project.feature.home.domain.orchestrator.child.TransactionListOrchestrator

/**
 * Parent orchestrator implementation that combines child component states.
 * 
 * This orchestrator:
 * - Creates and manages child orchestrators
 * - Combines their reactive states using Flow.combine()
 * - Determines priority ordering of components
 * - Handles component-specific error strategies (critical vs optional)
 * - Automatically rebuilds component list when any child state changes
 */
class HomeOrchestratorImpl : HomeOrchestrator {
    
    private val dateOrchestrator = DateHeaderOrchestrator()
    private val budgetOrchestrator = BudgetCardOrchestrator()
    private val transactionOrchestrator = TransactionListOrchestrator()
    
    private val _homeComponents = MutableStateFlow<List<HomeComponent>>(emptyList())
    override val homeComponents: StateFlow<List<HomeComponent>> = _homeComponents.asStateFlow()
    
    override suspend fun initialize() {
        // Initialize all child orchestrators
        dateOrchestrator.initialize()
        budgetOrchestrator.initialize()
        transactionOrchestrator.initialize()
        
        // Combine all child states reactively
        // Whenever any child state changes, rebuild the component list
        combine(
            flow = dateOrchestrator.componentState,
            flow2 = budgetOrchestrator.componentState,
            flow3 = transactionOrchestrator.componentState
        ) { dateHeader, budgetCard, transactionComponent ->
            buildComponentList(dateHeader, budgetCard, transactionComponent)
        }.collect { components ->
            _homeComponents.value = components
        }
    }
    
    /**
     * Builds the prioritized list of components based on child states.
     * 
     * Priority order:
     * 1. Date Header - Always shown if available
     * 2. Budget Card - Critical component
     * 3. Transaction List - Optional component, skips if null
     */
    private fun buildComponentList(
        dateHeader: HomeComponent.DateHeader?,
        budgetCard: HomeComponent.BudgetCard?,
        transactionComponent: HomeComponent?
    ): List<HomeComponent> {
        val components = mutableListOf<HomeComponent>()
        
        // Priority 1: Date Header (always show if available)
        dateHeader?.let { components.add(it) }
        
        // Priority 2: Budget Card
        if (budgetCard != null) {
            components.add(budgetCard)
        }
        
        // Priority 3: Transaction List (optional - skip if null)
        transactionComponent?.let { components.add(it) }
        
        return components
    }
}
