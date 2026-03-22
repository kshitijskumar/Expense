package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.navigation.Navigator
import org.example.project.ui.theme.ExpenseTrackerTheme

@Composable
@Preview
fun App() {
    ExpenseTrackerTheme {
        Navigator()
    }
}