package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.navigation.Navigator

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator()
    }
}