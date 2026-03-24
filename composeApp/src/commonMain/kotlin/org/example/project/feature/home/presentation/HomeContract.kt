package org.example.project.feature.home.presentation

import org.example.project.feature.home.domain.model.HomeComponent

sealed interface HomeIntent {
    data object Refresh : HomeIntent
    data object NavigateToAddExpense : HomeIntent
}

data class HomeState(
    val isLoading: Boolean = true,
    val components: List<HomeComponent> = emptyList()
)
