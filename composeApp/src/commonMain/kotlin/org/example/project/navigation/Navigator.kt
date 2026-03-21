package org.example.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject

@Composable
fun Navigator(
    navigationManager: NavigationManager = koinInject()
) {
    val navController = rememberNavController()

    LaunchedEffect(navController, navigationManager) {
        navigationManager.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.screen)
                }
                is NavigationEvent.NavigateBack -> {
                    navController.navigateUp()
                }
                is NavigationEvent.NavigateBackTo -> {
                    navController.popBackStack(
                        route = event.screen,
                        inclusive = event.inclusive
                    )
                }
                is NavigationEvent.ClearAndNavigate -> {
                    navController.navigate(event.screen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        // Screen registrations will be added in Phase 3
    }
}
