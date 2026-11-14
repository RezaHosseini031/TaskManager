package com.rezahosseini.taskmanager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─────────────────────────────────────────────
// Dark Theme
// ─────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(

    // Main accent → Purple
    primary = Color(0xFF5737F6),
    onPrimary = Color.White,

    // Secondary purple
    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF1F1535),

    // Orange remains as a small accent
    tertiary = Color(0xFFFF8A50),
    onTertiary = Color(0xFF3E1608),

    // Background
    background = Color(0xFF161311),
    onBackground = Color(0xFFF2ECE6),

    // Cards / surfaces
    surface = Color(0xFF1F1B18),
    onSurface = Color(0xFFF2ECE6),

    surfaceVariant = Color(0xFF2A2521),
    onSurfaceVariant = Color(0xFFC7B8AA),

    // Dividers / borders
    outline = Color(0xFF49413B),
    outlineVariant = Color(0xFF342F2A)

)


// ─────────────────────────────────────────────
// Light Theme
// ─────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(

    // Main accent → Orange
    primary = Color(0xFFFF7043),
    onPrimary = Color.White,

    // Secondary orange
    secondary = Color(0xFFFF8A50),
    onSecondary = Color.White,

    // Darker orange for additional accents
    tertiary = Color(0xFFE85D35),
    onTertiary = Color.White,

    // Background
    background = Color(0xFFFFF7F0),
    onBackground = Color(0xFF1F1B18),

    // Cards / surfaces
    surface = Color.White,
    onSurface = Color(0xFF1F1B18),

    surfaceVariant = Color(0xFFFFF1E8),
    onSurfaceVariant = Color(0xFF6F6259),

    // Dividers / borders
    outline = Color(0xFFD8CCC4),
    outlineVariant = Color(0xFFEDE2DA)
)


// ─────────────────────────────────────────────
// Theme
// ─────────────────────────────────────────────

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = colorScheme.surface.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}