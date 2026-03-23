package org.example.project.feature.home.domain.orchestrator

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for child component orchestrators.
 * Each orchestrator is responsible for managing a single home component's state.
 */
interface HomeComponentOrchestrator<T> {
    
    /**
     * Reactive state flow emitting the component data or null if unavailable.
     * 
     * Null means this component should not be shown (fail silently).
     * Parent orchestrator decides how to handle null states per component.
     */
    val componentState: StateFlow<T?>
    
    /**
     * Initialize the orchestrator and start collecting data.
     * Should be called once when orchestrator is created.
     * 
     * This method starts the reactive chain - subscribing to data sources
     * and emitting component states as data changes.
     */
    fun initialize()
}
