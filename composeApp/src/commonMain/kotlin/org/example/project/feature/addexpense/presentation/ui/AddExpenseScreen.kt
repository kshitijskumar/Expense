package org.example.project.feature.addexpense.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import expense.composeapp.generated.resources.Res
import expense.composeapp.generated.resources.ic_back
import org.example.project.feature.addexpense.presentation.AddExpenseIntent
import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.ui.components.PrimaryButton
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
            ExpenseFormContent(
                amount = state.amount,
                onAmountChange = { viewModel.onIntent(AddExpenseIntent.AmountChanged(it)) },
                date = state.date,
                onDatePickerShow = { viewModel.onIntent(AddExpenseIntent.DatePickerClicked) },
                onDatePickerDismiss = { viewModel.onIntent(AddExpenseIntent.DatePickerDismissed) },
                onDateSelected = { viewModel.onIntent(AddExpenseIntent.DateSelected(it)) },
                showDatePicker = state.showDatePicker,
                title = state.title,
                onTitleChange = { viewModel.onIntent(AddExpenseIntent.TitleChanged(it)) },
                categoryState = state.categoryState,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.onIntent(AddExpenseIntent.CategorySelected(it)) },
                onCategorySearchQueryChange = { viewModel.onIntent(AddExpenseIntent.CategorySearchQueryChanged(it)) },
                onCategoryAddNew = { viewModel.onIntent(AddExpenseIntent.CategoryAddNewClicked) },
                friendState = state.friendState,
                onFriendToggled = { viewModel.onIntent(AddExpenseIntent.FriendToggled(it)) },
                onFriendSearchQueryChange = { viewModel.onIntent(AddExpenseIntent.FriendSearchQueryChanged(it)) },
                onFriendAddNew = { viewModel.onIntent(AddExpenseIntent.FriendAddNewClicked) },
                notes = state.notes,
                onNotesChange = { viewModel.onIntent(AddExpenseIntent.NotesChanged(it)) },
                modifier = Modifier.weight(1f)
            )

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
