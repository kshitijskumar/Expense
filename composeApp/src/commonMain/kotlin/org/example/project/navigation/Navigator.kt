package org.example.project.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.feature.addexpense.presentation.EditExpenseViewModel
import org.example.project.feature.addexpense.presentation.ui.AddExpenseScreen
import org.example.project.feature.addexpense.presentation.ui.EditExpenseScreen
import org.example.project.feature.home.presentation.HomeViewModel
import org.example.project.feature.home.presentation.ui.HomeScreen
import org.example.project.feature.monthlyreport.presentation.ui.MonthlyReportScreen
import org.example.project.feature.category.CategorySelector
import org.example.project.feature.friend.FriendSelector
import org.example.project.feature.monthlyreport.presentation.MonthlyReportViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

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
        composable<Screen.Home> {
            val homeViewModel: HomeViewModel = koinInject()
            HomeScreen(viewModel = homeViewModel, navigationManager = navigationManager)
        }

        composable<Screen.AddExpense> {
            val viewModel: AddExpenseViewModel = koinInject()
            AddExpenseScreen(viewModel = viewModel)
        }

        composable<Screen.EditExpense> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditExpense>()
            val viewModel: EditExpenseViewModel = koinInject { parametersOf(route) }
            EditExpenseScreen(viewModel = viewModel)
        }

        composable<Screen.Friends> {
            ScreenPlaceholder(
                title = "Friends",
                navigationManager = navigationManager
            )
        }

        composable<Screen.AddFriend> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.AddFriend>()
            ScreenPlaceholder(
                title = "Add Friend",
                subtitle = route.friendId?.let { "Editing Friend ID: $it" } ?: "Adding New Friend",
                navigationManager = navigationManager
            )
        }

        composable<Screen.Categories> {
            ScreenPlaceholder(
                title = "Categories",
                navigationManager = navigationManager
            )
        }

        composable<Screen.MonthlyReport> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.MonthlyReport>()
            val viewmodel = koinInject<MonthlyReportViewModel> { parametersOf(route) }
            MonthlyReportScreen(viewmodel)
        }

        composable<Screen.FriendBalance> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.FriendBalance>()
            ScreenPlaceholder(
                title = "Friend Balance",
                subtitle = "Friend ID: ${route.friendId}",
                navigationManager = navigationManager
            )
        }

        composable<Screen.Settings> {
            ScreenPlaceholder(
                title = "Settings",
                navigationManager = navigationManager
            )
        }
    }
}

@Composable
private fun ScreenPlaceholder(
    title: String,
    subtitle: String? = null,
    navigationManager: NavigationManager,
    showNavigationButtons: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )

        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (showNavigationButtons) {
            Button(onClick = { navigationManager.navigateTo(Screen.AddExpense) }) {
                Text("Add Expense")
            }

            Button(onClick = { navigationManager.navigateTo(Screen.EditExpense(123)) }) {
                Text("Edit Expense (ID: 123)")
            }

            Button(onClick = { navigationManager.navigateTo(Screen.Friends) }) {
                Text("Friends")
            }

            Button(onClick = { navigationManager.navigateTo(Screen.Categories) }) {
                Text("Categories")
            }

            Button(onClick = { navigationManager.navigateTo(Screen.MonthlyReport(year = 2026, month = 3)) }) {
                Text("Monthly Report (March 2026)")
            }

            Button(onClick = { navigationManager.navigateTo(Screen.Settings) }) {
                Text("Settings")
            }
        }

        Button(onClick = { navigationManager.navigateBack() }) {
            Text("Back")
        }
    }
}
