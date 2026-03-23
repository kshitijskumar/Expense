package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator

/**
 * Orchestrator for the date header component.
 * Provides the current date formatted for display.
 */
class DateHeaderOrchestrator : HomeComponentOrchestrator<HomeComponent.DateHeader> {
    
    private val _componentState = MutableStateFlow<HomeComponent.DateHeader?>(null)
    override val componentState: StateFlow<HomeComponent.DateHeader?> = _componentState.asStateFlow()
    
    override suspend fun initialize() {
        // TODO: Replace with actual date formatting when DateTimeUtil is ready
        val formattedDate = "23 March" // Mock data
        _componentState.value = HomeComponent.DateHeader(formattedDate)
    }
}
