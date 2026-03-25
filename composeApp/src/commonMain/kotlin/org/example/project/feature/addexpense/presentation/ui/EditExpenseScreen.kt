package org.example.project.feature.addexpense.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import expense.composeapp.generated.resources.Res
import expense.composeapp.generated.resources.ic_back
import expense.composeapp.generated.resources.ic_delete
import org.example.project.feature.addexpense.presentation.EditExpenseIntent
import org.example.project.feature.addexpense.presentation.EditExpenseViewModel
import org.example.project.ui.components.PrimaryButton
import org.example.project.ui.theme.AppColors
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    viewModel: EditExpenseViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            val backBtnSize = 24.dp
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Expense",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onIntent(EditExpenseIntent.BackClicked) }
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
                    IconButton(
                        onClick = { viewModel.onIntent(EditExpenseIntent.DeleteClicked) }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Delete",
                            modifier = Modifier.size(backBtnSize)
                        )
                    }
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
                onAmountChange = { viewModel.onIntent(EditExpenseIntent.AmountChanged(it)) },
                date = state.date,
                onDatePickerShow = { viewModel.onIntent(EditExpenseIntent.DatePickerClicked) },
                onDatePickerDismiss = { viewModel.onIntent(EditExpenseIntent.DatePickerDismissed) },
                onDateSelected = { viewModel.onIntent(EditExpenseIntent.DateSelected(it)) },
                showDatePicker = state.showDatePicker,
                title = state.title,
                onTitleChange = { viewModel.onIntent(EditExpenseIntent.TitleChanged(it)) },
                categoryState = state.categoryState,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.onIntent(EditExpenseIntent.CategorySelected(it)) },
                onCategorySearchQueryChange = { viewModel.onIntent(EditExpenseIntent.CategorySearchQueryChanged(it)) },
                onCategoryAddNew = { viewModel.onIntent(EditExpenseIntent.CategoryAddNewClicked) },
                friendState = state.friendState,
                onFriendToggled = { viewModel.onIntent(EditExpenseIntent.FriendToggled(it)) },
                onFriendSearchQueryChange = { viewModel.onIntent(EditExpenseIntent.FriendSearchQueryChanged(it)) },
                onFriendAddNew = { viewModel.onIntent(EditExpenseIntent.FriendAddNewClicked) },
                notes = state.notes,
                onNotesChange = { viewModel.onIntent(EditExpenseIntent.NotesChanged(it)) },
                modifier = Modifier.weight(1f)
            )

            // Save and Delete Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Delete Button
                Button(
                    onClick = { viewModel.onIntent(EditExpenseIntent.DeleteClicked) },
                    enabled = !state.isDeleting && !state.isSaving,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        if (state.isDeleting) "DELETING..." else "DELETE",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Save Button
                PrimaryButton(
                    text = if (state.isSaving) "SAVING..." else "SAVE",
                    onClick = { viewModel.onIntent(EditExpenseIntent.SaveClicked) },
                    enabled = state.enableSaveBtn && !state.isDeleting && !state.isSaving,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (state.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(EditExpenseIntent.CancelDeleteClicked) },
            title = {
                Text("Delete Expense?")
            },
            text = {
                Text("Are you sure you want to delete this expense? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onIntent(EditExpenseIntent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(EditExpenseIntent.CancelDeleteClicked) }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
