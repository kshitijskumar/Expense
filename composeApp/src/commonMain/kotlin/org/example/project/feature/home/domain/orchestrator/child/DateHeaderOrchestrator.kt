package org.example.project.feature.home.domain.orchestrator.child

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.domain.orchestrator.HomeComponentOrchestrator
import org.example.project.util.DateTimeUtil
import org.example.project.util.getCurrentTimeMillis

/**
 * Orchestrator for the date header component.
 * Provides the current date formatted for display.
 */
class DateHeaderOrchestrator : HomeComponentOrchestrator<HomeComponent.DateHeader> {
    
    private val _componentState = MutableStateFlow<HomeComponent.DateHeader?>(null)
    override val componentState: StateFlow<HomeComponent.DateHeader?> = _componentState.asStateFlow()
    
    override fun initialize(scope: CoroutineScope) {
        val currentTime = DateTimeUtil.getCurrentTimeMillis()
        val formattedDate = DateTimeUtil.formatDateHeader(currentTime)
        _componentState.value = HomeComponent.DateHeader(formattedDate)
    }
}
