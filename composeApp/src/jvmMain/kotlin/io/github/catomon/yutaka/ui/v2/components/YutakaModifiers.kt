package io.github.catomon.yutaka.ui.v2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.catomon.yutaka.ui.WindowConfig
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter
import org.jetbrains.skia.Paint
import org.jetbrains.skia.RRect
import java.awt.MouseInfo
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter

/**
 * Tsukaremi-style undecorated window frame: soft glow, 4dp gradient border
 * and a rounded clip around the whole app.
 */
@Composable
fun Modifier.yutakaWindowDecoration(): Modifier {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(8.dp)

    return if (WindowConfig.isTransparent)
        this.padding(8.dp)
            .border(
                width = 4.dp,
                brush = Brush.verticalGradient(
                    0.0f to colors.windowBorderStart,
                    0.15f to colors.windowBorderStart,
                    0.5f to colors.windowBorderEnd,
                    1.0f to colors.windowBorderEnd,
                ),
                shape = shape,
            )
            .yutakaGlow(color = colors.glow, alpha = 0.75f, cornerRadius = 12.dp, shadowRadius = 4.dp)
            .clip(shape)
    else
        this.border(2.dp, colors.windowBorderStart, RectangleShape)
}

/** Skia-blurred rounded-rect glow, drawn behind the node. */
fun Modifier.yutakaGlow(
    color: Color = Color.White,
    alpha: Float = 0.7f,
    cornerRadius: Dp = 12.dp,
    shadowRadius: Dp = 6.dp,
) = drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            this.color = shadowColor
            maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, shadowRadius.toPx())
        }
        canvas.nativeCanvas.drawRRect(
            RRect.makeLTRB(0f, 0f, size.width, size.height, cornerRadius.toPx()),
            paint,
        )
    }
}

/** Opaque gradient panel used by the sidebar, gallery frame and menu. */
@Composable
fun Modifier.yutakaPanel(shape: Shape = RoundedCornerShape(8.dp)): Modifier {
    val colors = YutakaTheme.colors
    return this
        .clip(shape)
        .background(Brush.verticalGradient(listOf(colors.panelStart, colors.panelEnd)), shape)
        .border(
            width = 2.dp,
            color = colors.panelContent.copy(alpha = 0.22f),
            shape = shape,
        )
}

/** Opaque gradient card for gallery thumbnails. */
@Composable
fun Modifier.yutakaItemBackground(
    shape: Shape = RoundedCornerShape(10.dp),
    highlighted: Boolean = false,
): Modifier {
    val colors = YutakaTheme.colors
    val brush = if (highlighted)
        Brush.verticalGradient(listOf(colors.itemSelectedStart, colors.itemSelectedEnd))
    else
        Brush.verticalGradient(listOf(colors.itemStart, colors.itemEnd))

    return this
        .clip(shape)
        .background(brush, shape)
        .border(
            width = if (highlighted) 3.dp else 2.dp,
            color = if (highlighted) colors.itemSelectedOutline else colors.panelBorderStart,
            shape = shape,
        )
}

/**
 * Drags the AWT window when the pointer is pressed on this node. Used on the
 * header only, so dragging inside the viewer pans the image instead.
 */
fun Modifier.yutakaWindowDrag(window: Window): Modifier = pointerInput(window) {
    val handler = DragHandler(window)
    awaitEachGesture {
        awaitFirstDown()
        handler.register()
    }
}

private class DragHandler(private val window: Window) {
    private var location = window.location
    private var pointStart = MouseInfo.getPointerInfo().location

    private val dragListener = object : MouseMotionAdapter() {
        override fun mouseDragged(event: MouseEvent) = drag()
    }
    private val removeListener = object : MouseAdapter() {
        override fun mouseReleased(event: MouseEvent) {
            window.removeMouseMotionListener(dragListener)
            window.removeMouseListener(this)
        }
    }

    fun register() {
        location = window.location
        pointStart = MouseInfo.getPointerInfo().location
        window.addMouseListener(removeListener)
        window.addMouseMotionListener(dragListener)
    }

    private fun drag() {
        val point = MouseInfo.getPointerInfo().location
        window.setLocation(
            location.x + (point.x - pointStart.x),
            location.y + (point.y - pointStart.y),
        )
    }
}
