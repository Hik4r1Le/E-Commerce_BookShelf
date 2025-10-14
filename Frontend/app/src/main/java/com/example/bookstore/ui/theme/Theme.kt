package com.example.bookstore.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// You can customize these later
private val LightColors = lightColorScheme(
    primary = Color(0xFFF5D3C4),
    secondary = Color(0xFFFFE6E6),
    background = Color(0xFFFFFFFF),
    onPrimary = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7A5F5F),
    secondary = Color(0xFFB48E8E),
    background = Color(0xFF121212),
    onPrimary = Color.White
)

@Composable
fun BookstoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography, // from your Type.kt
        content = content
    )
}
