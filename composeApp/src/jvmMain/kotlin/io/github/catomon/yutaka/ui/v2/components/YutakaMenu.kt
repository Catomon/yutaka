package io.github.catomon.yutaka.ui.v2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme

@Composable
fun YutakaMenuOverlay(
    onRefresh: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenSafebooru: () -> Unit,
    onOpenGitHub: () -> Unit,
    onExit: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val panelInteraction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClose,
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 18.dp, top = 68.dp)
                .width(224.dp)
                .yutakaPanel()
                .padding(vertical = 8.dp)
                .clickable(
                    interactionSource = panelInteraction,
                    indication = null,
                    onClick = {},
                ),
        ) {
            YutakaMenuEntry("Refresh gallery") {
                onRefresh()
                onClose()
            }
            YutakaMenuEntry("Open Downloads") {
                onOpenDownloads()
                onClose()
            }
            YutakaMenuEntry("Safebooru website") {
                onOpenSafebooru()
                onClose()
            }
            YutakaMenuEntry("GitHub") {
                onOpenGitHub()
                onClose()
            }
            YutakaMenuEntry("Exit app") {
                onExit()
            }
        }
    }
}

@Composable
private fun YutakaMenuEntry(
    text: String,
    onClick: () -> Unit,
) {
    val colors = YutakaTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isHovered)
                    Brush.horizontalGradient(listOf(colors.itemStart, colors.itemEnd))
                else
                    Brush.horizontalGradient(listOf(colors.panelStart, colors.panelEnd))
            )
            .hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        YutakaOutlinedText(
            text = text,
            fillColor = colors.panelContent,
            outlineColor = colors.headerOutline,
            fontSize = 15.sp,
            outlineSize = 3.5f,
            maxLines = 1,
        )
    }
}
