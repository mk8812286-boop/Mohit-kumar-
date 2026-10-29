package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    primaryContainer = WarmCream,
    onPrimaryContainer = DarkChocolate,
    secondary = AntiqueGold,
    onSecondary = DarkChocolate,
    secondaryContainer = SoftBeige,
    onSecondaryContainer = DarkChocolate,
    tertiary = DarkChocolate,
    onTertiary = WarmCream,
    background = WarmCream,
    onBackground = CharcoalText,
    surface = Color.White,
    onSurface = CharcoalText,
    surfaceVariant = SoftBeige,
    onSurfaceVariant = MutedText,
    outline = AntiqueGold,
    outlineVariant = CardBorderGold,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = TerracottaLight,
    onPrimary = DarkChocolate,
    primaryContainer = TerracottaDark,
    onPrimaryContainer = WarmCream,
    secondary = AntiqueGoldLight,
    onSecondary = DarkChocolate,
    secondaryContainer = DarkChocolate,
    onSecondaryContainer = WarmCream,
    tertiary = AntiqueGold,
    onTertiary = DarkChocolateDark,
    background = DarkChocolateDark,
    onBackground = WarmCream,
    surface = DarkChocolate,
    onSurface = WarmCream,
    surfaceVariant = Color(0xFF381F12),
    onSurfaceVariant = SoftBeige,
    outline = AntiqueGold,
    outlineVariant = AntiqueGoldLight,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun PrajapatiKalaTheme(
    darkTheme: Boolean = false, // Default to rich warm cream traditional look
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
