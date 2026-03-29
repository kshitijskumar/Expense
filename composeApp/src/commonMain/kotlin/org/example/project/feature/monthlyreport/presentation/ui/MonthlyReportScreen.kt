package org.example.project.feature.monthlyreport.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import expense.composeapp.generated.resources.Res
import expense.composeapp.generated.resources.ic_back
import org.example.project.feature.monthlyreport.domain.model.CategorySpend
import org.example.project.feature.monthlyreport.domain.model.FriendSpend
import org.example.project.feature.monthlyreport.presentation.MonthlyReportIntent
import org.example.project.feature.monthlyreport.presentation.MonthlyReportState
import org.example.project.feature.monthlyreport.presentation.MonthlyReportViewModel
import org.example.project.ui.components.TransactionRow
import org.example.project.ui.theme.AppColors
import org.example.project.util.CurrencyUtil
import org.example.project.util.DateTimeUtil
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyReportScreen(
    viewModel: MonthlyReportViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            MonthlyReportTopBar(
                state = state,
                onBackClick = { viewModel.onIntent(MonthlyReportIntent.BackClicked) },
                onPreviousMonth = { viewModel.onIntent(MonthlyReportIntent.PreviousMonthClicked) },
                onNextMonth = { viewModel.onIntent(MonthlyReportIntent.NextMonthClicked) },
                onMoveToCurrentMonth = { viewModel.onIntent(MonthlyReportIntent.MoveToCurrentMonthClicked) }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TotalSpentCard(totalSpent = state.totalSpent)
                }

                item {
                    CategoriesAndFriendsRow(
                        categories = state.topCategories,
                        friends = state.topFriends,
                        onViewAllCategories = {
                            viewModel.onIntent(MonthlyReportIntent.ViewAllCategoriesClicked(state.allCategorySpendings))
                        },
                        onViewAllFriends = {
                            viewModel.onIntent(MonthlyReportIntent.ViewAllFriendsClicked(state.allFriendSpendings))
                        }
                    )
                }

                state.transactionsByDate.forEach { group ->
                    stickyHeader(key = group.dateLabel) {
                        DateHeaderComponent(group.dateLabel)
                    }

                    items(
                        items = group.transactions,
                        key = { it.id }
                    ) { expense ->
                        TransactionRow(
                            title = expense.title,
                            amount = expense.amount,
                            category = expense.category,
                            participantCount = expense.participants.size,
                            onClick = {
                                viewModel.onIntent(MonthlyReportIntent.TransactionClicked(expense.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonthlyReportTopBar(
    state: MonthlyReportState,
    onBackClick: () -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMoveToCurrentMonth: () -> Unit
) {
    val backBtnSize = 24.dp
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Transactions",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(backBtnSize),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                Spacer(Modifier.size(backBtnSize))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppColors.current.background,
                titleContentColor = AppColors.current.textPrimary,
                navigationIconContentColor = AppColors.current.textPrimary
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Text("◄", fontSize = 16.sp)
            }

            Text(
                text = DateTimeUtil.getMonthYearLabel(state.selectedMonth ?: 1, state.selectedYear ?: 2026),
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(onClick = onNextMonth) {
                Text("►", fontSize = 16.sp)
            }

            Button(
                onClick = onMoveToCurrentMonth,
                enabled = state.showMoveToCurrentMonth
            ) {
                Text("Today", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun TotalSpentCard(totalSpent: Long?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.current.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total spent",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.current.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (totalSpent != null) CurrencyUtil.toDisplayAmount(totalSpent) else "Calculating...",
                style = MaterialTheme.typography.displaySmall,
                color = AppColors.current.textPrimary
            )
        }
    }
}

@Composable
private fun CategoriesAndFriendsRow(
    categories: List<CategorySpend>,
    friends: List<FriendSpend>,
    onViewAllCategories: () -> Unit,
    onViewAllFriends: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CategoriesCard(
            categories = categories,
            onViewAll = onViewAllCategories,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        FriendsCard(
            friends = friends,
            onViewAll = onViewAllFriends,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@Composable
private fun CategoriesCard(
    categories: List<CategorySpend>,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onViewAll() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.current.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.labelMedium,
                color = AppColors.current.textPrimary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (categories.isEmpty()) {
                Text(
                    text = "No categories",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.current.textSecondary,
                    modifier = Modifier
                )
            } else {
                categories.forEach { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category.category.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.current.textPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = CurrencyUtil.toDisplayAmount(category.totalAmount),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.current.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "+ view all",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.current.primary
            )
        }
    }
}

@Composable
private fun FriendsCard(
    friends: List<FriendSpend>,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onViewAll() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.current.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
        ) {
            Text(
                text = "Friends",
                style = MaterialTheme.typography.labelMedium,
                color = AppColors.current.textPrimary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (friends.isEmpty()) {
                Text(
                    text = "No split",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.current.textSecondary,
                    modifier = Modifier
                )
            } else {
                friends.forEach { friend ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = friend.friend.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.current.textPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = CurrencyUtil.toDisplayAmount(friend.amountOwed),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.current.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "+ view all",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.current.primary
            )
        }
    }
}

@Composable
private fun DateHeaderComponent(dateLabel: String) {
    Text(
        text = dateLabel,
        style = MaterialTheme.typography.headlineSmall,
        color = AppColors.current.textPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.current.background)
            .padding(vertical = 8.dp)
    )
}
