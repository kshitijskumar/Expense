package org.example.project.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.ui.theme.AppColors
import org.example.project.util.DateTimeUtil
import org.example.project.util.getCurrentTimeMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    showDatePicker: Boolean,
    onShowDatePickerChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = formatDate(selectedDate)
    
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = AppColors.current.grid,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onShowDatePickerChange(true) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.current.textPrimary
            )
        }
    }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        
        DatePickerDialog(
            onDismissRequest = { onShowDatePickerChange(false) },
            confirmButton = {
                TextButton(
                    text = "OK",
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(millis)
                        }
                        onShowDatePickerChange(false)
                    }
                )
            },
            dismissButton = {
                TextButton(
                    text = "Cancel",
                    onClick = { onShowDatePickerChange(false) }
                )
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier
            )
        }
    }
}

private fun formatDate(epochMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    
    // Get today's date using platform-specific current timestamp
    val todayInstant = Instant.fromEpochMilliseconds(DateTimeUtil.getCurrentTimeMillis())
    val today = todayInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    
    return when {
        dateTime.date == today.date -> "Today"
        else -> "${dateTime.dayOfMonth} ${dateTime.month.name}"
    }
}