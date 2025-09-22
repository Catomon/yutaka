package io.github.catomon.yutaka.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Yutaka Kobayakawa colors (approximate hex)
val pastelPink = Color(0xFFFFD1DC)
val softBlue = Color(0xFFAED8E6)
val green = Color(0xFF6AA84F)
val white = Color(0xFFFFFFFF)
val darkBackground = Color(0xFF121212)
val lightBackground = Color(0xFFFFFFFF)

val LightColorScheme = lightColorScheme(
    primary = pastelPink,
    onPrimary = white,
    primaryContainer = Color(0xFFFFE5EB), // lighter pastel pink
    onPrimaryContainer = pastelPink,
    secondary = softBlue,
    onSecondary = white,
    secondaryContainer = Color(0xFFD8ECF5), // lighter soft blue
    onSecondaryContainer = softBlue,
    tertiary = green,
    onTertiary = white,
    tertiaryContainer = Color(0xFFD8EAD1), // lighter green
    onTertiaryContainer = green,
    background = lightBackground,
    onBackground = Color(0xFF000000),
    surface = lightBackground,
    onSurface = Color(0xFF000000),
    error = Color(0xFFB00020),
    onError = white
)

val DarkColorScheme = darkColorScheme(
    primary = pastelPink,
    onPrimary = darkBackground,
    primaryContainer = Color(0xFF732A38), // darker pastel pink
    onPrimaryContainer = pastelPink,
    secondary = softBlue,
    onSecondary = darkBackground,
    secondaryContainer = Color(0xFF4A7B93), // darker soft blue
    onSecondaryContainer = softBlue,
    tertiary = green,
    onTertiary = darkBackground,
    tertiaryContainer = Color(0xFF3B5A2E), // darker green
    onTertiaryContainer = green,
    background = darkBackground,
    onBackground = white,
    surface = darkBackground,
    onSurface = white,
    error = Color(0xFFCF6679),
    onError = darkBackground
)

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
internal fun AppTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember(systemIsDark) { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        MaterialTheme(
            colorScheme = if (isDark) DarkColorScheme else LightColorScheme,
            content = { Surface(content = content, modifier = modifier) }
        )
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
