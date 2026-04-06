package org.example.project.feature.friendspendanalysis.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import expense.composeapp.generated.resources.Res
import expense.composeapp.generated.resources.ic_back
import expense.composeapp.generated.resources.ic_dropdown
import org.example.project.domain.model.FriendSpendDetail
import org.example.project.feature.friendspendanalysis.presentation.FriendSpendAnalysisIntent
import org.example.project.feature.friendspendanalysis.presentation.FriendSpendAnalysisViewModel
import org.example.project.ui.components.BarEntry
import org.example.project.ui.components.HorizontalBarChart
import org.example.project.ui.components.TransactionRow
import org.example.project.ui.theme.AppColors
import org.example.project.util.CurrencyUtil
import org.example.project.util.DateTimeUtil
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendSpendAnalysisScreen(
    viewModel: FriendSpendAnalysisViewModel,
    month: Int,
    year: Int
) {
    val state by viewModel.state.collectAsState()
    val colors = AppColors.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = DateTimeUtil.getMonthYearLabel(month, year),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(FriendSpendAnalysisIntent.BackClicked) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Spacer(Modifier.size(24.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.textPrimary,
                    navigationIconContentColor = colors.textPrimary
                )
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else if (state.friendSpendDetails.isEmpty()) {
            Text(
                text = "No shared expenses found for the month",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 32.dp)
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
                    HorizontalBarChart(
                        entries = state.friendSpendDetails.map {
                            BarEntry(
                                label = it.friend.name,
                                value = it.totalAmountOwed.toFloat()
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        valueFormatter = { CurrencyUtil.toDisplayAmount(it.toLong()) }
                    )
                }

                items(
                    items = state.friendSpendDetails,
                    key = { it.friend.id }
                ) { detail ->
                    FriendSpendCard(
                        detail = detail,
                        isExpanded = state.expandedFriendId == detail.friend.id,
                        onTap = {
                            viewModel.onIntent(
                                FriendSpendAnalysisIntent.FriendRowTapped(detail.friend.id)
                            )
                        },
                        onExpenseClicked = {
                            viewModel.onIntent(FriendSpendAnalysisIntent.ExpenseClicked(it))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendSpendCard(
    detail: FriendSpendDetail,
    isExpanded: Boolean,
    onTap: () -> Unit,
    onExpenseClicked: (Long) -> Unit
) {
    val colors = AppColors.current
    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detail.friend.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = CurrencyUtil.toDisplayAmount(detail.totalAmountOwed),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary
                )
                Spacer(Modifier.width(12.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_dropdown),
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = colors.textSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(iconRotation)
                )
            }

            if (isExpanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = colors.divider
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    detail.transactions.forEach { tx ->
                        TransactionRow(
                            title = tx.title,
                            amount = tx.amount,
                            category = tx.category,
                            participantCount = tx.participants.size,
                            onClick = { onExpenseClicked(tx.id) }
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
