package io.github.catomon.yutaka.ui.v2.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Two-layer icon: a slightly larger outline behind the tinted fill. */
@Composable
fun YutakaIcon(
    painter: Painter,
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    outlineColor: Color? = null,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        if (outlineColor != null) {
            Image(
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.tint(outlineColor),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(scaleX = 1.3f, scaleY = 1.3f),
            )
        }
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun YutakaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = true,
    enabled: Boolean = true,
    fontSize: Int = 14,
) {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(8.dp)
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val background = when {
        primary && hovered && enabled -> Brush.verticalGradient(listOf(colors.itemStart, colors.itemEnd))
        primary -> Brush.verticalGradient(listOf(colors.controlStart, colors.controlEnd))
        hovered && enabled -> Brush.verticalGradient(listOf(colors.itemSelectedStart, colors.itemSelectedEnd))
        else -> Brush.verticalGradient(listOf(colors.backgroundStart, colors.backgroundEnd))
    }
    val fill = when {
        !enabled -> if (primary) colors.controlContent.copy(alpha = 0.55f) else colors.disabled
        primary -> colors.controlContent
        else -> colors.itemSelectedContent
    }
    val outline = if (primary) colors.headerOutline else colors.outline

    Box(
        modifier = modifier
            .clip(shape)
            .background(background, shape)
            .border(
                width = 2.dp,
                color = colors.panelBorderStart,
                shape = shape,
            )
            .hoverable(interaction)
            .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
            .clickable(enabled = enabled, onClickLabel = text, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        YutakaOutlinedText(
            text = text,
            fillColor = fill,
            outlineColor = outline,
            fontSize = fontSize.sp,
            outlineSize = 3.5f,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}

/** Plain glyph button used by the header and viewer toolbars. */
@Composable
fun YutakaGlyphButton(
    glyph: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    fontSize: Int = 18,
    fillColor: Color = YutakaTheme.colors.headerContent,
    outlineColor: Color = YutakaTheme.colors.headerOutline,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .hoverable(remember { MutableInteractionSource() })
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        YutakaOutlinedText(
            text = glyph,
            fillColor = fillColor,
            outlineColor = outlineColor,
            fontSize = fontSize.sp,
            outlineSize = 3.5f,
            maxLines = 1,
        )
    }
}

@Composable
fun <T> YutakaSegmented(
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(8.dp)

    Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        options.forEach { (value, label) ->
            val active = value == selected
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(
                        if (active)
                            Brush.verticalGradient(listOf(colors.itemSelectedStart, colors.itemSelectedEnd))
                        else
                            Brush.verticalGradient(listOf(colors.backgroundStart, colors.backgroundEnd)),
                        shape,
                    )
                    .border(
                        width = 2.dp,
                        color = if (active) colors.itemSelectedOutline else colors.panelBorderStart,
                        shape = shape,
                    )
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable(onClickLabel = label) { onSelect(value) }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center,
            ) {
                YutakaOutlinedText(
                    text = label,
                    fillColor = if (active) colors.itemSelectedContent else colors.text,
                    outlineColor = if (active) colors.itemSelectedOutline else colors.outline,
                    fontSize = 13.sp,
                    outlineSize = 3f,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun YutakaSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .height(38.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(colors.backgroundStart, colors.backgroundEnd)), shape)
            .border(2.dp, colors.panelBorderStart, shape)
            .padding(start = 10.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        YutakaOutlinedText(
            text = "tags:",
            fillColor = colors.textSecondary,
            outlineColor = colors.outline,
            fontSize = 13.sp,
            outlineSize = 3f,
            maxLines = 1,
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown &&
                        (event.key == Key.Enter || event.key == Key.NumPadEnter)
                    ) {
                        onSubmit()
                        true
                    } else false
                },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = colors.text,
                fontSize = 15.sp,
            ),
            cursorBrush = SolidColor(colors.headerStart),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = "lucky_star",
                            color = colors.textSecondary.copy(alpha = 0.6f),
                            fontSize = 15.sp,
                        )
                    }
                    innerTextField()
                }
            },
        )

        YutakaButton(
            text = "Search",
            onClick = onSubmit,
            fontSize = 13,
        )
    }
}

@Composable
fun YutakaBadge(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = YutakaTheme.colors.accent,
    content: Color = YutakaTheme.colors.onAccent,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .border(1.dp, content.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = content,
            fontSize = 11.sp,
            maxLines = 1,
        )
    }
}

/** Small star button used on gallery cards and in the viewer top bar. */
@Composable
fun YutakaStarButton(
    starred: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
) {
    val colors = YutakaTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(listOf(colors.backgroundStart, colors.backgroundEnd)),
                CircleShape,
            )
            .border(2.dp, colors.panelBorderStart, CircleShape)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(
                onClickLabel = if (starred) "Remove from saved posts" else "Save post",
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size * 0.56f)) {
            val side = this.size.minDimension
            val cx = this.size.width / 2f
            val cy = this.size.height / 2f
            val outer = side / 2f
            val inner = outer * 0.45f
            val path = Path()

            for (i in 0 until 10) {
                val radius = if (i % 2 == 0) outer else inner
                val angle = (-90f + i * 36f) * (PI.toFloat() / 180f)
                val x = cx + cos(angle) * radius
                val y = cy + sin(angle) * radius
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawPath(
                path = path,
                color = colors.panelBorderStart,
                style = Stroke(width = 2.6.dp.toPx(), join = StrokeJoin.Round),
            )

            if (starred) {
                drawPath(path = path, color = Color(0xFFF2C14E))
            } else {
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(listOf(colors.panelStart, colors.panelEnd)),
                )
            }
        }
    }
}
