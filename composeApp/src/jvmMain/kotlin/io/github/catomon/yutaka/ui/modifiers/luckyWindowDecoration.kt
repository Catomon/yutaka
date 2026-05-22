package io.github.catomon.yutaka.ui.modifiers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.github.catomon.yutaka.ui.theme.green
import io.github.catomon.yutaka.ui.util.darken
import io.github.catomon.yutaka.ui.viewmodel.WindowConfig

@Composable
fun Modifier.luckyWindowDecoration2(): Modifier {
    val density = LocalDensity.current
    val shadowColor = MaterialTheme.colorScheme.background.darken(0.25f)
    val glowColor = MaterialTheme.colorScheme.background.darken(0.25f)
    return if (WindowConfig.isTransparent)
        this.padding(8.dp).customShadow().drawBehind {
            val radDp = with(density) { 12.dp.toPx() }
            drawRoundRect(
                color = glowColor,
                topLeft = Offset(0f, with(density) { -2.dp.toPx() }),
                size = this.size.copy(),
                cornerRadius = CornerRadius(radDp)
            )
            drawRoundRect(
                color = shadowColor,
                topLeft = Offset(0f, with(density) { 2.dp.toPx() }),
                size = this.size.copy(),
                cornerRadius = CornerRadius(radDp)
            )
        }.clip(RoundedCornerShape(8.dp))
    else
        this.blurredShadow(cornerRadius = 0.dp)
            .border(
                width = 2.dp,
                color = shadowColor,
                shape = RectangleShape
            )
}

@Composable
fun Modifier.luckyWindowDecoration(): Modifier {
    val density = LocalDensity.current
    val shadowColor = Color.White
    val glowColor = MaterialTheme.colorScheme.surfaceContainerLow
    val borderGradient = Brush.verticalGradient(
        0.0f to green,
        0.15f to green,
        0.5f to green,
        1.0f to green,
    )

    return if (WindowConfig.isTransparent)
        this.padding(8.dp)
            .border(
                width = 2.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(12.dp)
            )
            .customShadow(color = Color.White).clip(RoundedCornerShape(12.dp))
            .padding(2.dp)
    else
        this.blurredShadow(cornerRadius = 0.dp, color = Color.White)
            .border(
                width = 2.dp,
                color = shadowColor,
                shape = RectangleShape
            )
}