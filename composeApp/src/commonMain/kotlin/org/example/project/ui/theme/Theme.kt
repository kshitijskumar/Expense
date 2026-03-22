package org.example.project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val Material3DarkColorScheme = darkColorScheme(
    primary = DarkColorScheme.primary,
    onPrimary = DarkColorScheme.onPrimary,
    primaryContainer = DarkColorScheme.primaryContainer,
    onPrimaryContainer = DarkColorScheme.primary,
    
    secondary = DarkColorScheme.primaryVariant,
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF3D3300),
    onSecondaryContainer = DarkColorScheme.primaryVariant,
    
    tertiary = DarkColorScheme.iconTint,
    onTertiary = Color(0xFF000000),
    
    error = DarkColorScheme.error,
    onError = Color(0xFF000000),
    errorContainer = DarkColorScheme.errorContainer,
    onErrorContainer = DarkColorScheme.error,
    
    background = DarkColorScheme.background,
    onBackground = DarkColorScheme.textPrimary,
    
    surface = DarkColorScheme.surface,
    onSurface = DarkColorScheme.textPrimary,
    surfaceVariant = DarkColorScheme.surfaceVariant,
    onSurfaceVariant = DarkColorScheme.textSecondary,
    surfaceContainer = DarkColorScheme.surfaceContainer,
    
    outline = DarkColorScheme.textTertiary,
    outlineVariant = DarkColorScheme.grid,
    
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF6750A4)
)

@Composable
fun ExpenseTrackerTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppColors provides DarkColorScheme) {
        MaterialTheme(
            colorScheme = Material3DarkColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
