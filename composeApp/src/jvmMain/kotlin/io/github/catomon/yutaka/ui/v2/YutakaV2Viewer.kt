package io.github.catomon.yutaka.ui.v2

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.ui.v2.components.YutakaBadge
import io.github.catomon.yutaka.ui.v2.components.YutakaButton
import io.github.catomon.yutaka.ui.v2.components.YutakaGlyphButton
import io.github.catomon.yutaka.ui.v2.components.YutakaOutlinedText
import io.github.catomon.yutaka.ui.v2.components.YutakaStarButton
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YutakaViewer(
    post: Post,
    viewModel: MainViewModel,
    starred: Boolean,
    onToggleStar: () -> Unit,
    onClose: () -> Unit,
    onOpenSource: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    var scale by remember(post.id) { mutableFloatStateOf(1f) }
    var offset by remember(post.id) { mutableStateOf(Offset.Zero) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(post.id) {
        scale = 1f
        offset = Offset.Zero
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape -> {
                        onClose()
                        true
                    }
                    Key.DirectionLeft -> {
                        viewModel.prevPost()
                        true
                    }
                    Key.DirectionRight -> {
                        viewModel.nextPost()
                        true
                    }
                    Key.D -> {
                        viewModel.downloadViewedPost()
                        true
                    }
                    Key.S -> {
                        onToggleStar()
                        true
                    }
                    Key.Equals -> {
                        scale = (scale * 1.2f).coerceAtMost(6f)
                        true
                    }
                    Key.Minus -> {
                        scale = (scale / 1.2f).coerceAtLeast(0.5f)
                        if (scale <= 1f) offset = Offset.Zero
                        true
                    }
                    Key.Zero -> {
                        scale = 1f
                        offset = Offset.Zero
                        true
                    }
                    else -> false
                }
            },
    ) {
        AsyncImage(
            model = post.largeUri.ifBlank { post.originalUri },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.16f),
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.25f), Color.Black.copy(alpha = 0.72f))
                    )
                )
        )

        Column(Modifier.fillMaxSize()) {
            ViewerTopBar(
                post = post,
                isDownloading = viewModel.isDownloading,
                progress = viewModel.progress,
                downloaded = viewModel.downloaded,
                starred = starred,
                onToggleStar = onToggleStar,
                onBack = onClose,
                onDownload = viewModel::downloadViewedPost,
                onOpenSource = onOpenSource,
                onCopyUrl = { copyToClipboard(post.originalUri) },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clipToBounds(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onPointerEvent(PointerEventType.Scroll) { event ->
                            val delta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                            if (delta != 0f) {
                                scale = (scale * if (delta < 0f) 1.12f else 0.89f).coerceIn(0.5f, 6f)
                                if (scale <= 1f) offset = Offset.Zero
                            }
                        }
                        .pointerInput(post.id) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.5f, 6f)
                                offset = if (scale <= 1f) Offset.Zero else offset + pan
                            }
                        }
                        .pointerInput(post.id) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scale = if (scale > 1f) 1f else 2f
                                    offset = Offset.Zero
                                }
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = post.originalUri,
                        contentDescription = post.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y,
                            ),
                    )
                }

                ViewerNavButton(
                    glyph = "‹",
                    description = "Previous image",
                    onClick = viewModel::prevPost,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp),
                )

                ViewerNavButton(
                    glyph = "›",
                    description = "Next image",
                    onClick = viewModel::nextPost,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp),
                )

                YutakaBadge(
                    text = "${(scale * 100).roundToInt()}%",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    background = colors.headerEnd.copy(alpha = 0.9f),
                    content = colors.headerContent,
                )
            }

            ViewerTagBar(post = post, onTagClick = viewModel::applyTags, onClose = onClose)
        }
    }
}

@Composable
private fun ViewerTopBar(
    post: Post,
    isDownloading: Boolean,
    progress: Int,
    downloaded: Boolean,
    starred: Boolean,
    onToggleStar: () -> Unit,
    onBack: () -> Unit,
    onDownload: () -> Unit,
    onOpenSource: () -> Unit,
    onCopyUrl: () -> Unit,
) {
    val colors = YutakaTheme.colors
    val shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(colors.headerStart, colors.headerEnd)))
            .border(2.dp, colors.headerContent.copy(alpha = 0.22f), shape)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        YutakaGlyphButton(
            glyph = "‹",
            description = "Back to gallery",
            onClick = onBack,
            size = 30.dp,
            fontSize = 20,
        )

        Column(modifier = Modifier.weight(1f)) {
            YutakaOutlinedText(
                text = post.title.ifBlank { "Post ${post.id}" },
                fillColor = colors.headerContent,
                outlineColor = colors.headerOutline,
                fontSize = 17.sp,
                outlineSize = 4f,
                maxLines = 1,
                contentAlignment = Alignment.CenterStart,
            )
            YutakaOutlinedText(
                text = "${post.host}  ·  score ${post.score}  ·  ${post.rating.ifBlank { "?" }}",
                fillColor = colors.headerContent.copy(alpha = 0.85f),
                outlineColor = colors.headerOutline,
                fontSize = 11.sp,
                outlineSize = 3f,
                maxLines = 1,
                contentAlignment = Alignment.CenterStart,
            )
        }

        YutakaStarButton(
            starred = starred,
            onClick = onToggleStar,
            size = 30.dp,
        )

        YutakaButton(
            text = when {
                isDownloading -> "Saving $progress%"
                downloaded -> "Saved"
                else -> "Download"
            },
            onClick = onDownload,
            enabled = !isDownloading,
            fontSize = 13,
        )

        YutakaButton(
            text = "Source",
            onClick = onOpenSource,
            primary = false,
            fontSize = 13,
        )

        YutakaButton(
            text = "Copy URL",
            onClick = onCopyUrl,
            primary = false,
            fontSize = 13,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun ViewerTagBar(
    post: Post,
    onTagClick: (String) -> Unit,
    onClose: () -> Unit,
) {
    val colors = YutakaTheme.colors
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val tags = remember(post.tags, post.author) {
        val clean = post.tags.filter { it.isNotBlank() }.distinct()
        val artistTags = post.author.split(" ").filter { it.isNotBlank() }
        if (artistTags.isEmpty()) clean
        else artistTags.filter { it in clean } + clean.filterNot { it in artistTags }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(Brush.verticalGradient(listOf(colors.panelStart, colors.panelEnd)))
            .border(2.dp, colors.panelContent.copy(alpha = 0.18f), RoundedCornerShape(0.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .onPointerEvent(PointerEventType.Scroll) { event ->
                    val delta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                    if (delta != 0f) {
                        scope.launch { scrollState.scrollBy(delta * 90f) }
                    }
                }
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.itemStart.copy(alpha = 0.65f))
                        .border(1.dp, colors.panelContent.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable {
                            onTagClick(tag)
                            onClose()
                        }
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                ) {
                    YutakaOutlinedText(
                        text = tag,
                        fillColor = colors.panelContent,
                        outlineColor = colors.headerOutline,
                        fontSize = 11.sp,
                        outlineSize = 3f,
                        maxLines = 1,
                    )
                }
            }
        }

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 10.dp, vertical = 2.dp),
            style = ScrollbarStyle(
                minimalHeight = 24.dp,
                thickness = 4.dp,
                shape = RoundedCornerShape(2.dp),
                hoverDurationMillis = 300,
                unhoverColor = colors.panelContent.copy(alpha = 0.35f),
                hoverColor = colors.panelContent.copy(alpha = 0.72f),
            ),
        )
    }
}

@Composable
private fun ViewerNavButton(
    glyph: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(colors.headerEnd.copy(alpha = 0.75f))
            .border(2.dp, colors.panelBorderStart.copy(alpha = 0.7f), CircleShape)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        YutakaOutlinedText(
            text = glyph,
            fillColor = colors.headerContent,
            outlineColor = colors.headerOutline,
            fontSize = 30.sp,
            outlineSize = 4f,
            maxLines = 1,
        )
    }
}
