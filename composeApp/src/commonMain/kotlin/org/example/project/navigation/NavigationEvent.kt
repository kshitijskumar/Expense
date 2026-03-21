package org.example.project.navigation

sealed interface NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent
    data object NavigateBack : NavigationEvent
    data class NavigateBackTo(val screen: Screen, val inclusive: Boolean = false) : NavigationEvent
    data class ClearAndNavigate(val screen: Screen) : NavigationEvent
}
