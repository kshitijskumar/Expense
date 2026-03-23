package org.example.project.feature.home.domain.orchestrator

import kotlinx.coroutines.flow.StateFlow
import org.example.project.feature.home.domain.model.HomeComponent

/**
 * Parent orchestrator that combines child component states.
 * Determines priority ordering of components and handles component-specific error strategies.
 * 
 * This orchestrator is fully reactive - when any child component state changes,
 * the combined home components list is automatically rebuilt and emitted.
 */
interface HomeOrchestrator {
    
    /**
     * Combined state of all home components in priority order.
     * Reactively updates when any child state changes.
     * 
     * The list is ordered by priority (date header first, then budget, then transactions).
     * Components that fail may be replaced with error cards or omitted entirely
     * based on whether they are critical or optional.
     */
    val homeComponents: StateFlow<List<HomeComponent>>
    
    /**
     * Initialize orchestrator and all child orchestrators.
     * 
     * This starts the reactive chain:
     * 1. Initializes all child orchestrators
     * 2. Combines their state flows
     * 3. Emits the combined list whenever any child state changes
     * 
     * Should be called once during ViewModel initialization.
     */
    suspend fun initialize()
}
