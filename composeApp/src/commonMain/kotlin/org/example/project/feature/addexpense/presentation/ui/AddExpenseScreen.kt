package org.example.project.feature.addexpense.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import expense.composeapp.generated.resources.Res
import expense.composeapp.generated.resources.ic_back
import org.example.project.feature.addexpense.presentation.AddExpenseIntent
import org.example.project.feature.addexpense.presentation.AddExpenseState
import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.ui.components.AddChip
import org.example.project.ui.components.AppAmountTextField
import org.example.project.ui.components.AppOutlinedTextField
import org.example.project.ui.components.AppTextField
import org.example.project.ui.components.CategoryChip
import org.example.project.ui.components.DatePickerField
import org.example.project.ui.components.FriendChip
import org.example.project.ui.components.PrimaryButton
import org.example.project.ui.components.SearchResultsDropdown
import org.example.project.ui.components.SearchTextField
import org.example.project.ui.theme.AppColors
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            val backBtnSize = 24.dp
            TopAppBar(
                title = {
                    Text(
                        text = "Add Expense",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                        },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onIntent(AddExpenseIntent.BackClicked) }
                    ) {
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Amount Section
                AmountSection(
                    amount = state.amount,
                    onAmountChange = { viewModel.onIntent(AddExpenseIntent.AmountChanged(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Date Section
                DateSection(
                    selectedDate = state.date,
                    showDatePicker = state.showDatePicker,
                    onDatePickerShow = { viewModel.onIntent(AddExpenseIntent.DatePickerClicked) },
                    onDatePickerDismiss = { viewModel.onIntent(AddExpenseIntent.DatePickerDismissed) },
                    onDateSelected = { viewModel.onIntent(AddExpenseIntent.DateSelected(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Title Section
                TitleSection(
                    title = state.title,
                    onTitleChange = { viewModel.onIntent(AddExpenseIntent.TitleChanged(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = AppColors.current.grid)
                Spacer(modifier = Modifier.height(24.dp))

                // Category Section
                CategorySection(
                    state = state,
                    onCategorySelected = { viewModel.onIntent(AddExpenseIntent.CategorySelected(it)) },
                    onSearchQueryChange = { viewModel.onIntent(AddExpenseIntent.CategorySearchQueryChanged(it)) },
                    onAddNew = { viewModel.onIntent(AddExpenseIntent.CategoryAddNewClicked) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = AppColors.current.grid)
                Spacer(modifier = Modifier.height(24.dp))

                // Friends Section
                FriendsSection(
                    state = state,
                    onFriendToggled = { viewModel.onIntent(AddExpenseIntent.FriendToggled(it)) },
                    onSearchQueryChange = { viewModel.onIntent(AddExpenseIntent.FriendSearchQueryChanged(it)) },
                    onAddNew = { viewModel.onIntent(AddExpenseIntent.FriendAddNewClicked) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = AppColors.current.grid)
                Spacer(modifier = Modifier.height(24.dp))

                // Notes Section
                NotesSection(
                    notes = state.notes,
                    onNotesChange = { viewModel.onIntent(AddExpenseIntent.NotesChanged(it)) }
                )

                Spacer(Modifier.height(32.dp))
            }

            // Save Button
            PrimaryButton(
                text = "SAVE",
                onClick = { viewModel.onIntent(AddExpenseIntent.SaveClicked) },
                enabled = state.enableSaveBtn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun AmountSection(
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How much?",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.current.textSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        AppAmountTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.wrapContentWidth(),
            placeholder = "₹ 0.0",
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
private fun DateSection(
    selectedDate: Long,
    showDatePicker: Boolean,
    onDatePickerShow: () -> Unit,
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DatePickerField(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            showDatePicker = showDatePicker,
            onShowDatePickerChange = { show ->
                if (show) onDatePickerShow() else onDatePickerDismiss()
            },
            modifier = Modifier.width(140.dp)
        )
    }
}

@Composable
private fun TitleSection(
    title: String,
    onTitleChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Title",
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.current.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        AppOutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = "Lunch with team"
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySection(
    state: AddExpenseState,
    onCategorySelected: (org.example.project.domain.model.CategoryModel) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddNew: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.current.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Category chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val displayCategories by remember(state.categoryState) {
                derivedStateOf {
                    val output = state.categoryState.basicCategories.toMutableList()
                    state.categoryState.recentlyAdded?.let { recentlyAdded ->
                        output.add(recentlyAdded)
                    }
                    output
                }
            }
            displayCategories.forEach { category ->
                CategoryChip(
                    label = category.name,
                    isSelected = state.selectedCategory?.id == category.id,
                    onClick = { onCategorySelected(category) }
                )
            }

            val selectedCategory = state.categoryState.selectedCategory
            val basicContainsSelectedCategory = displayCategories.contains(selectedCategory)
            if (!basicContainsSelectedCategory && selectedCategory != null) {
                // user searched and added category separately
                CategoryChip(
                    label = selectedCategory.name,
                    isSelected = state.selectedCategory?.id == selectedCategory.id,
                    onClick = { onCategorySelected(selectedCategory) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Search field with dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchTextField(
                value = state.categoryState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search categories..."
            )
            
            // Search results dropdown
            SearchResultsDropdown(
                query = state.categoryState.searchQuery,
                results = state.categoryState.searchResults,
                isVisible = true,
                onItemClick = { category ->
                    onCategorySelected(category)
                },
                onAddClick = onAddNew,
                itemLabel = { it.name },
                emptyResultsMessage = "No categories found",
                addButtonText = "Add \"${state.categoryState.searchQuery}\""
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FriendsSection(
    state: AddExpenseState,
    onFriendToggled: (org.example.project.domain.model.FriendModel) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddNew: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Split with Friends (Optional)",
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.current.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.friendState.allFriends.forEach { friend ->
                FriendChip(
                    name = friend.name,
                    isSelected = state.friendState.selectedFriends.contains(friend),
                    onClick = { onFriendToggled(friend) }
                )
            }
        }

        if (state.friendState.allFriends.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        // Search field with dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchTextField(
                value = state.friendState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search friends..."
            )
            
            // Search results dropdown
            SearchResultsDropdown(
                query = state.friendState.searchQuery,
                results = state.friendState.searchResults,
                isVisible = true,
                onItemClick = { friend ->
                    onFriendToggled(friend)
                },
                onAddClick = onAddNew,
                itemLabel = { it.name },
                emptyResultsMessage = "No friends found",
                addButtonText = "Add \"${state.friendState.searchQuery}\""
            )
        }
        
//        // Split preview
//        if (state.selectedFriends.isNotEmpty() && state.amount.isNotBlank()) {
//            Spacer(modifier = Modifier.height(12.dp))
//
//            SplitPreview(
//                amount = state.amount,
//                friendCount = state.selectedFriends.size
//            )
//        }
    }
}

@Composable
private fun SplitPreview(
    amount: String,
    friendCount: Int
) {
    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val splitAmount = if (friendCount > 0) amountValue / (friendCount + 1) else 0.0
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ℹ️",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
//        Text(
//            text = "Split equally: Rs. %.2f each ($friendCount friend${if (friendCount > 1) "s" else ""})".format(splitAmount),
//            style = MaterialTheme.typography.bodyMedium,
//            color = AppColors.current.textSecondary
//        )
    }
}

@Composable
private fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Notes (Optional)",
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.current.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        AppOutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            placeholder = "Add notes...",
            maxLines = 3,
            singleLine = false
        )
    }
}

private fun getCategoryEmoji(categoryName: String): String {
    return when (categoryName.lowercase()) {
        "food" -> "🍔"
        "travel" -> "🚗"
        "groceries" -> "🛒"
        "entertainment" -> "🎬"
        "health" -> "🏥"
        "shopping" -> "🛍️"
        "bills" -> "📱"
        "other" -> "📦"
        else -> "💰"
    }
}
