package org.example.project.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.theme.AppColors

@Composable
fun <T> SearchResultsDropdown(
    query: String,
    results: List<T>,
    isVisible: Boolean,
    onItemClick: (T) -> Unit,
    onAddClick: () -> Unit,
    itemLabel: (T) -> String,
    emptyResultsMessage: String,
    addButtonText: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible && query.isNotEmpty(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(12.dp),
            color = AppColors.current.surface,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                if (results.isNotEmpty()) {
                    // Show search results
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        results.forEachIndexed { index, item ->
                            SearchResultItem(
                                label = itemLabel(item),
                                onClick = { onItemClick(item) }
                            )
                            
                            if (index < results.size - 1) {
                                HorizontalDivider(
                                    color = AppColors.current.grid,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                } else {
                    // Show "Add New" option
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = emptyResultsMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.current.textSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        AddButton(
                            text = addButtonText,
                            onClick = onAddClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.current.textPrimary,
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = "Select",
            style = MaterialTheme.typography.labelMedium,
            color = AppColors.current.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = AppColors.current.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.current.onPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}
