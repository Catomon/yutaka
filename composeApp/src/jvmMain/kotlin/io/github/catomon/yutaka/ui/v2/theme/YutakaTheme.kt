package io.github.catomon.yutaka.ui.v2.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.IndieFlower_Regular

val LocalYutakaColors = staticCompositionLocalOf { YutakaThemes.default }

@Composable
fun YutakaTheme(content: @Composable () -> Unit) {
    val colors = YutakaThemes.default
    val fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular))

    CompositionLocalProvider(LocalYutakaColors provides colors) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colors.accent,
                onPrimary = colors.onAccent,
                secondary = colors.accentSoft,
                onSecondary = colors.onAccent,
                background = colors.backgroundMid,
                onBackground = colors.text,
                surface = colors.panelStart,
                onSurface = colors.panelContent,
                surfaceVariant = colors.itemStart,
                onSurfaceVariant = colors.itemContent,
                outline = colors.panelBorderStart,
                error = colors.headerStart,
                onError = colors.headerContent,
            ),
            typography = yutakaTypography(fontFamily),
            content = content,
        )
    }
}

private fun yutakaTypography(fontFamily: FontFamily): Typography {
    val t = Typography()
    return t.copy(
        displayLarge = t.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = t.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = t.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = t.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = t.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = t.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = t.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = t.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = t.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = t.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = t.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = t.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = t.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = t.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = t.labelSmall.copy(fontFamily = fontFamily),
    )
}

object YutakaTheme {
    val colors: YutakaColors
        @Composable get() = LocalYutakaColors.current
}
