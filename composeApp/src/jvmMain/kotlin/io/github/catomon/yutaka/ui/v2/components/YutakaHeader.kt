package io.github.catomon.yutaka.ui.v2.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.star_sparkle

@Composable
fun YutakaHeader(
    onOpenMenu: () -> Unit,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    dragModifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(colors.headerStart, colors.headerEnd)))
            .border(2.dp, colors.headerContent.copy(alpha = 0.22f), shape)
            .padding(horizontal = 14.dp)
            .then(dragModifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(96.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable(onClickLabel = "Open menu", onClick = onOpenMenu),
                contentAlignment = Alignment.Center,
            ) {
                YutakaIcon(
                    painter = painterResource(Res.drawable.star_sparkle),
                    tint = colors.headerContent,
                    size = 24.dp,
                    outlineColor = colors.headerOutline,
                )
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                YutakaOutlinedText(
                    text = "Yutaka · Safebooru",
                    fillColor = colors.headerContent,
                    outlineColor = colors.headerOutline,
                    fontSize = 22.sp,
                    outlineSize = 4.5f,
                    maxLines = 1,
                )
            }
        }

        Row(
            modifier = Modifier.width(96.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YutakaWindowButton(
                description = "Minimize",
                contentColor = colors.headerContent,
                onClick = onMinimize,
            ) { color ->
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = 2.2.dp.toPx(),
                )
            }

            YutakaWindowButton(
                description = "Maximize or restore",
                contentColor = colors.headerContent,
                onClick = onMaximize,
            ) { color ->
                drawRect(
                    color = color,
                    topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(size.width - 2.dp.toPx(), size.height - 2.dp.toPx()),
                    style = Stroke(width = 2f),
                )
            }

            YutakaWindowButton(
                description = "Close",
                contentColor = colors.headerContent,
                onClick = onClose,
            ) { color ->
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.2.dp.toPx(),
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 2.2.dp.toPx(),
                )
            }
        }
    }
}

@Composable
private fun YutakaWindowButton(
    description: String,
    contentColor: Color,
    onClick: () -> Unit,
    content: androidx.compose.ui.graphics.drawscope.DrawScope.(Color) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(start = 6.dp)
            .size(28.dp)
            .clip(CircleShape)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(12.dp)) {
            content(contentColor)
        }
    }
}
