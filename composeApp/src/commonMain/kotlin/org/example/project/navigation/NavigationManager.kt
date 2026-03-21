package org.example.project.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationManager {
    private val _navigationEvents = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvents: Flow<NavigationEvent> = _navigationEvents.receiveAsFlow()

    fun navigateTo(screen: Screen) {
        _navigationEvents.trySend(NavigationEvent.NavigateTo(screen))
    }

    fun navigateBack() {
        _navigationEvents.trySend(NavigationEvent.NavigateBack)
    }

    fun navigateBackTo(screen: Screen, inclusive: Boolean = false) {
        _navigationEvents.trySend(NavigationEvent.NavigateBackTo(screen, inclusive))
    }

    fun clearAndNavigate(screen: Screen) {
        _navigationEvents.trySend(NavigationEvent.ClearAndNavigate(screen))
    }
}
