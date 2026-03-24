package org.example.project.feature.home.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.ExpenseSummaryModel
import org.example.project.feature.home.domain.model.HomeComponent
import org.example.project.feature.home.presentation.HomeIntent
import org.example.project.feature.home.presentation.HomeViewModel
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.theme.AppColors
import org.example.project.util.CurrencyUtil
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    navigationManager: NavigationManager = koinInject()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(HomeIntent.NavigateToAddExpense) },
                containerColor = AppColors.current.primary
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge,
                    color = AppColors.current.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isLoading && state.components.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.components,
                        key = { componentKey(it) }
                    ) { component ->
                        HomeComponentRow(
                            component = component,
                            onExpenseClick = { expenseId ->
                                viewModel.onIntent(HomeIntent.NavigateToExpenseDetail(expenseId))
                            }
                        )
                    }
                }
            }
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
        is HomeComponent.DateHeader -> DateHeaderComponent(component)
        is HomeComponent.BudgetCard -> BudgetCardComponent(component)
        is HomeComponent.TransactionList -> TransactionListComponent(component, onExpenseClick)
        HomeComponent.EmptyTransactions -> EmptyTransactionsComponent()
        is HomeComponent.ErrorCard -> ErrorCardComponent(component)
    }
}

@Composable
private fun DateHeaderComponent(component: HomeComponent.DateHeader) {
    Text(
        text = component.formattedDate,
        style = MaterialTheme.typography.headlineLarge,
        color = AppColors.current.textPrimary
    )
}

@Composable
private fun BudgetCardComponent(component: HomeComponent.BudgetCard) {
    val colors = AppColors.current
    val arcColor = if (component.isOverBudget) colors.error else colors.primary
    val percentage = (component.progress * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Spent this month",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textSecondary
                )
                Text(
                    text = CurrencyUtil.toDisplayAmount(component.monthlySpend),
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Monthly budget",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textSecondary
                )
                Text(
                    text = if (component.monthlyBudget != null) {
                        CurrencyUtil.toDisplayAmount(component.monthlyBudget)
                    } else {
                        "—"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary
                )
            }

            // Donut chart: a 270° arc starting at bottom-left
            // Explains Canvas params:
            //   strokeWidth: thickness of the arc line (10dp)
            //   inset: padding to account for stroke width so arc doesn't get cut off
            //   arcSize: size of the bounding box for the arc (canvas size - stroke width)
            //   topLeft: offset to position the arc (inset, inset) to center it
            //   startAngle: 135° = bottom-left (0° = 3 o'clock, 90° = 6 o'clock, 180° = 9 o'clock)
            //   sweepAngle: 270° full sweep for background; progress*270° for filled portion
            //   useCenter: false = draws an arc, true would fill a pie slice
            //   StrokeCap.Round: rounds the arc endpoints for a softer look
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(88.dp)
            ) {
                val trackColor = colors.textTertiary
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 10.dp.toPx()
                    val inset = strokeWidth / 2f
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(inset, inset)

                    // Background track: full 270° light grey arc
                    drawArc(
                        color = trackColor,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress arc: filled portion (0–270° based on progress ratio)
                    // Clamped to [0, 1] to avoid arc going over 360°
                    drawArc(
                        color = arcColor,
                        startAngle = 135f,
                        sweepAngle = component.progress.coerceIn(0f, 1f) * 270f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun TransactionListComponent(
    component: HomeComponent.TransactionList,
    onExpenseClick: (Long) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = component.date,
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.current.textSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        component.transactions.forEach { tx ->
            TransactionRow(tx = tx, onClick = { onExpenseClick(tx.id) })
        }
    }
}

@Composable
private fun TransactionRow(tx: ExpenseSummaryModel, onClick: () -> Unit) {
    val colors = AppColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left: title + category chip
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = tx.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.textPrimary
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = colors.surfaceVariant
                ) {
                    Text(
                        text = tx.category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Right: amount + participant count
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = CurrencyUtil.toDisplayAmount(tx.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.textPrimary
                )
                if (tx.participantCount > 0) {
                    Text(
                        text = "with ${tx.participantCount} others",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionsComponent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No transactions yet",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.current.textSecondary
        )
    }
}

@Composable
private fun ErrorCardComponent(component: HomeComponent.ErrorCard) {
    val colors = AppColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.errorContainer)
    ) {
        Text(
            text = "${component.componentType}: ${component.message}",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.error,
            modifier = Modifier.padding(12.dp)
        )
    }
}
