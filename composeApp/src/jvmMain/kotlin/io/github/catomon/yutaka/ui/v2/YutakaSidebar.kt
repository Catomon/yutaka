package io.github.catomon.yutaka.ui.v2

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.catomon.yutaka.ui.util.LoadingStatus
import io.github.catomon.yutaka.ui.v2.components.YutakaButton
import io.github.catomon.yutaka.ui.v2.components.YutakaOutlinedText
import io.github.catomon.yutaka.ui.v2.components.yutakaPanel
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun YutakaSidebar(
    viewModel: MainViewModel,
    visibleCount: Int,
    totalCount: Int,
    starredCount: Int,
    starredOnly: Boolean,
    onToggleStarred: () -> Unit,
    onOpenDownloads: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val scrollState = rememberScrollState()

    Box(modifier = modifier.yutakaPanel()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            YutakaOutlinedText(
                text = "BROWSE",
                fillColor = colors.panelContent,
                outlineColor = colors.headerOutline,
                fontSize = 18.sp,
                outlineSize = 4f,
                maxLines = 1,
            )

            InfoRow("Tags", viewModel.tags.ifBlank { "lucky_star" })
            InfoRow("Page", if (starredOnly) "saved" else "${viewModel.page + 1}")
            InfoRow("Showing", "$visibleCount / $totalCount")
            InfoRow(
                label = "Status",
                value = when (viewModel.status) {
                    LoadingStatus.LOADING -> "Loading..."
                    LoadingStatus.SUCCESS -> "Ready"
                    LoadingStatus.FAIL -> "Connection failed"
                },
                valueColor = when (viewModel.status) {
                    LoadingStatus.LOADING -> colors.panelContentSecondary
                    LoadingStatus.SUCCESS -> Color(0xFFBDEBB0)
                    LoadingStatus.FAIL -> Color(0xFFFFC7C0)
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                YutakaButton(
                    text = "Refresh",
                    onClick = viewModel::reload,
                    modifier = Modifier.weight(1f),
                    fontSize = 13,
                )
                YutakaButton(
                    text = "More",
                    onClick = viewModel::loadMore,
                    modifier = Modifier.weight(1f),
                    primary = false,
                    enabled = !viewModel.isLoading,
                    fontSize = 13,
                )
            }

            SidebarDivider()

            YutakaOutlinedText(
                text = "STARRED",
                fillColor = colors.panelContentSecondary,
                outlineColor = colors.headerOutline,
                fontSize = 14.sp,
                outlineSize = 3.5f,
                maxLines = 1,
            )
            YutakaButton(
                text = if (starredOnly) "Show all posts" else "Saved posts ($starredCount)",
                onClick = onToggleStarred,
                modifier = Modifier.fillMaxWidth(),
                primary = starredOnly,
                fontSize = 12,
            )

            SidebarDivider()

            YutakaOutlinedText(
                text = "QUICK TAGS",
                fillColor = colors.panelContentSecondary,
                outlineColor = colors.headerOutline,
                fontSize = 14.sp,
                outlineSize = 3.5f,
                maxLines = 1,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                listOf(
                    "lucky_star",
                    "izumi_konata",
                    "hiiragi_kagami",
                    "takara_miyuki",
                    "hiiragi_tsukasa",
                    "kobayakawa_yutaka",
                ).forEach { tag ->
                    YutakaButton(
                        text = tag,
                        onClick = { viewModel.applyTags(tag) },
                        primary = false,
                        fontSize = 12,
                    )
                }
            }

            SidebarDivider()

            YutakaOutlinedText(
                text = "SAVING",
                fillColor = colors.panelContentSecondary,
                outlineColor = colors.headerOutline,
                fontSize = 14.sp,
                outlineSize = 3.5f,
                maxLines = 1,
            )
            YutakaOutlinedText(
                text = "Downloads land in Downloads/Yutaka.",
                fillColor = colors.panelContent,
                outlineColor = colors.headerOutline,
                fontSize = 12.sp,
                outlineSize = 3f,
                maxLines = 2,
                contentAlignment = Alignment.CenterStart,
            )
            YutakaButton(
                text = "Open Downloads",
                onClick = onOpenDownloads,
                modifier = Modifier.fillMaxWidth(),
                primary = false,
                fontSize = 12,
            )

            SidebarDivider()

            YutakaOutlinedText(
                text = "SHORTCUTS",
                fillColor = colors.panelContentSecondary,
                outlineColor = colors.headerOutline,
                fontSize = 14.sp,
                outlineSize = 3.5f,
                maxLines = 1,
            )
            ShortcutRow("Left / Right", "previous / next")
            ShortcutRow("D", "download image")
            ShortcutRow("S", "star post")
            ShortcutRow("+ / -", "zoom")
            ShortcutRow("0", "reset zoom")
            ShortcutRow("Esc", "close viewer")
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(vertical = 8.dp),
            style = ScrollbarStyle(
                minimalHeight = 24.dp,
                thickness = 6.dp,
                shape = RoundedCornerShape(3.dp),
                hoverDurationMillis = 300,
                unhoverColor = colors.panelContent.copy(alpha = 0.4f),
                hoverColor = colors.panelContent.copy(alpha = 0.8f),
            ),
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = YutakaTheme.colors.panelContent,
) {
    val colors = YutakaTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YutakaOutlinedText(
            text = label,
            fillColor = colors.panelContentSecondary,
            outlineColor = colors.headerOutline,
            fontSize = 12.sp,
            outlineSize = 3f,
            maxLines = 1,
        )
        YutakaOutlinedText(
            text = value,
            fillColor = valueColor,
            outlineColor = colors.headerOutline,
            fontSize = 12.sp,
            outlineSize = 3f,
            maxLines = 1,
        )
    }
}

@Composable
private fun ShortcutRow(keys: String, action: String) {
    val colors = YutakaTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        YutakaOutlinedText(
            text = keys,
            fillColor = colors.panelContent,
            outlineColor = colors.headerOutline,
            fontSize = 11.sp,
            outlineSize = 3f,
            maxLines = 1,
        )
        YutakaOutlinedText(
            text = action,
            fillColor = colors.panelContentSecondary,
            outlineColor = colors.headerOutline,
            fontSize = 11.sp,
            outlineSize = 3f,
            maxLines = 1,
        )
    }
}

@Composable
private fun SidebarDivider() {
    androidx.compose.material3.HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .height(1.dp),
        color = YutakaTheme.colors.panelContent.copy(alpha = 0.25f),
    )
}
