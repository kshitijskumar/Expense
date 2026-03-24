package org.example.project.feature.home.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.presentation.HomeIntent
import org.example.project.feature.home.presentation.HomeViewModel
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    navigationManager: NavigationManager = koinInject()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium
        )

        if (state.isLoading && state.components.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.components, key = { componentKey(it) }) { component ->
                    HomeComponentRow(component, onExpenseClick = { expenseId ->
                        viewModel.onIntent(HomeIntent.NavigateToExpenseDetail(expenseId))
                    })
                }
            }
        }

        HorizontalDivider()

        Button(onClick = { viewModel.onIntent(HomeIntent.NavigateToAddExpense) }) {
            Text("Add expense")
        }

        Button(onClick = { navigationManager.navigateTo(Screen.Friends) }) {
            Text("Friends")
        }

        Button(onClick = { navigationManager.navigateTo(Screen.Categories) }) {
            Text("Categories")
        }
    }
}

private fun componentKey(component: HomeComponent): String {
    return when (component) {
        is HomeComponent.DateHeader -> "date_${component.formattedDate}"
        is HomeComponent.BudgetCard -> "budget_${component.monthlySpend}_${component.monthlyBudget}"
        is HomeComponent.TransactionList -> "tx_${component.date}_${component.transactions.size}"
        HomeComponent.EmptyTransactions -> "empty_tx"
        is HomeComponent.ErrorCard -> "err_${component.componentType}"
    }
}

@Composable
private fun HomeComponentRow(component: HomeComponent, onExpenseClick: (Long) -> Unit) {
    when (component) {
        is HomeComponent.DateHeader -> {
            Text(
                text = component.formattedDate,
                style = MaterialTheme.typography.titleLarge
            )
        }

        is HomeComponent.BudgetCard -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Budget card (placeholder)", style = MaterialTheme.typography.labelMedium)
                Text("Spent: ${component.monthlySpend} (minor units)")
                Text("Budget: ${component.monthlyBudget ?: "—"}")
                Text("Progress: ${component.progress}, over: ${component.isOverBudget}")
            }
        }

        is HomeComponent.TransactionList -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Transactions — ${component.date}",
                    style = MaterialTheme.typography.titleMedium
                )
                component.transactions.forEach { tx ->
                    Text(
                        text = "${tx.title} · ${tx.amount} · ${tx.category.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExpenseClick(tx.id) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        HomeComponent.EmptyTransactions -> {
            Text("No transactions (empty state placeholder)")
        }

        is HomeComponent.ErrorCard -> {
            Text(
                text = "${component.componentType}: ${component.message}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
