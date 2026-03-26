package org.example.project.feature.addexpense.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.FriendModel
import org.example.project.feature.category.CategorySelectionState
import org.example.project.feature.friend.FriendSelectionState
import org.example.project.ui.components.AppAmountTextField
import org.example.project.ui.components.AppOutlinedTextField
import org.example.project.ui.components.CategoryChip
import org.example.project.ui.components.DatePickerField
import org.example.project.ui.components.FriendChip
import org.example.project.ui.components.SearchResultsDropdown
import org.example.project.ui.components.SearchTextField
import org.example.project.ui.theme.AppColors
import org.example.project.util.CurrencyUtil.INR_CURRENCY_SYMBOL

/**
 * Reusable expense form content composable.
 * Contains all form sections for adding/editing expenses.
 * Parent screens handle buttons and screen-specific logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormContent(
    amount: String,
    onAmountChange: (String) -> Unit,
    date: Long,
    onDatePickerShow: () -> Unit,
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit,
    showDatePicker: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    categoryState: CategorySelectionState,
    selectedCategory: CategoryModel?,
    onCategorySelected: (CategoryModel) -> Unit,
    onCategorySearchQueryChange: (String) -> Unit,
    onCategoryAddNew: () -> Unit,
    friendState: FriendSelectionState,
    onFriendToggled: (FriendModel) -> Unit,
    onFriendSearchQueryChange: (String) -> Unit,
    onFriendAddNew: () -> Unit,
    notes: String,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Amount Section
        AmountSection(
            amount = amount,
            onAmountChange = onAmountChange
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Date Section
        DateSection(
            selectedDate = date,
            showDatePicker = showDatePicker,
            onDatePickerShow = onDatePickerShow,
            onDatePickerDismiss = onDatePickerDismiss,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title Section
        TitleSection(
            title = title,
            onTitleChange = onTitleChange
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = AppColors.current.grid)
        Spacer(modifier = Modifier.height(24.dp))

        // Category Section
        CategorySection(
            categoryState = categoryState,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            onSearchQueryChange = onCategorySearchQueryChange,
            onAddNew = onCategoryAddNew
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = AppColors.current.grid)
        Spacer(modifier = Modifier.height(24.dp))

        // Friends Section
        FriendsSection(
            friendState = friendState,
            onFriendToggled = onFriendToggled,
            onSearchQueryChange = onFriendSearchQueryChange,
            onAddNew = onFriendAddNew
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = AppColors.current.grid)
        Spacer(modifier = Modifier.height(24.dp))

        // Notes Section
        NotesSection(
            notes = notes,
            onNotesChange = onNotesChange
        )

        Spacer(Modifier.height(32.dp))
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
            placeholder = "${INR_CURRENCY_SYMBOL} 0.0",
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
            placeholder = "Eg. Friday night dinner",
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySection(
    categoryState: CategorySelectionState,
    selectedCategory: CategoryModel?,
    onCategorySelected: (CategoryModel) -> Unit,
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
            val displayCategories by remember(categoryState) {
                derivedStateOf {
                    val output = categoryState.basicCategories.toMutableList()
                    categoryState.recentlyAdded?.let { recentlyAdded ->
                        output.add(recentlyAdded)
                    }
                    output
                }
            }
            displayCategories.forEach { category ->
                CategoryChip(
                    label = category.name,
                    isSelected = selectedCategory?.id == category.id,
                    onClick = { onCategorySelected(category) }
                )
            }

            val selectedCategoryFromState = categoryState.selectedCategory
            val basicContainsSelectedCategory = displayCategories.contains(selectedCategoryFromState)
            if (!basicContainsSelectedCategory && selectedCategoryFromState != null) {
                // user searched and added category separately
                CategoryChip(
                    label = selectedCategoryFromState.name,
                    isSelected = selectedCategory?.id == selectedCategoryFromState.id,
                    onClick = { onCategorySelected(selectedCategoryFromState) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search field with dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchTextField(
                value = categoryState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search categories..."
            )

            // Search results dropdown
            SearchResultsDropdown(
                query = categoryState.searchQuery,
                results = categoryState.searchResults,
                isVisible = true,
                onItemClick = { category ->
                    onCategorySelected(category)
                },
                onAddClick = onAddNew,
                itemLabel = { it.name },
                emptyResultsMessage = "No categories found",
                addButtonText = "Add \"${categoryState.searchQuery}\""
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FriendsSection(
    friendState: FriendSelectionState,
    onFriendToggled: (FriendModel) -> Unit,
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
            friendState.allFriends.forEach { friend ->
                FriendChip(
                    name = friend.name,
                    isSelected = friendState.selectedFriends.contains(friend),
                    onClick = { onFriendToggled(friend) }
                )
            }
        }

        if (friendState.allFriends.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Search field with dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchTextField(
                value = friendState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search friends..."
            )

            // Search results dropdown
            SearchResultsDropdown(
                query = friendState.searchQuery,
                results = friendState.searchResults,
                isVisible = true,
                onItemClick = { friend ->
                    onFriendToggled(friend)
                },
                onAddClick = onAddNew,
                itemLabel = { it.name },
                emptyResultsMessage = "No friends found",
                addButtonText = "Add \"${friendState.searchQuery}\""
            )
        }
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
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
    }
}
