package org.example.project.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    // Background colors
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceContainer: Color,
    
    // Primary colors (Yellow accent)
    val primary: Color,
    val primaryVariant: Color,
    val primaryContainer: Color,
    val onPrimary: Color,
    
    // Text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    
    // Additional UI colors
    val grid: Color,
    val iconTint: Color,
    val divider: Color,
    
    // Status colors
    val error: Color,
    val errorContainer: Color,
    val success: Color,
    val warning: Color,
)

internal val DarkColorScheme = AppColorScheme(
    // Background colors
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    surfaceContainer = Color(0xFF252525),
    
    // Primary colors (Yellow accent)
    primary = Color(0xFFFFD700),
    primaryVariant = Color(0xFFFFC107),
    primaryContainer = Color(0xFF3D3300),
    onPrimary = Color(0xFF000000),
    
    // Text colors
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFB3B3B3),
    textTertiary = Color(0xFF808080),
    
    // Additional UI colors
    grid = Color(0xFF2A2A2A),
    iconTint = Color(0xFFE0E0E0),
    divider = Color(0xFF2A2A2A),
    
    // Status colors
    error = Color(0xFFCF6679),
    errorContainer = Color(0xFF93000A),
    success = Color(0xFF4CAF50),
    warning = Color(0xFFFF9800),
)

val LocalAppColors = staticCompositionLocalOf { DarkColorScheme }

object AppColors {
    val current: AppColorScheme
        @Composable
        get() = LocalAppColors.current
}
