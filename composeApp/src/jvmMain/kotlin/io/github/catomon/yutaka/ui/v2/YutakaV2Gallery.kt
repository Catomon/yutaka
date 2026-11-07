package io.github.catomon.yutaka.ui.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.ui.util.LoadingStatus
import io.github.catomon.yutaka.ui.v2.components.YutakaBadge
import io.github.catomon.yutaka.ui.v2.components.YutakaButton
import io.github.catomon.yutaka.ui.v2.components.YutakaOutlinedText
import io.github.catomon.yutaka.ui.v2.components.YutakaStarButton
import io.github.catomon.yutaka.ui.v2.components.yutakaItemBackground
import io.github.catomon.yutaka.ui.v2.components.yutakaPanel
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources._894jfct80ip51

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YutakaGallery(
    posts: List<Post>,
    isLoading: Boolean,
    status: LoadingStatus,
    scrollResetKey: Any,
    sourceTags: List<String>,
    starredOnly: Boolean,
    isStarred: (Post) -> Boolean,
    onToggleStar: (Post) -> Unit,
    onOpenPost: (Post) -> Unit,
    onOpenSource: (Post) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val gridState = rememberLazyGridState()
    var lastRequestedCount by remember { mutableIntStateOf(-1) }

    LaunchedEffect(scrollResetKey) {
        gridState.scrollToItem(0)
    }

    LaunchedEffect(gridState, posts.size, isLoading) {
        if (posts.isEmpty()) return@LaunchedEffect
        snapshotFlow {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisible >= posts.size - 5
        }
            .distinctUntilChanged()
            .collect { atEnd ->
                if (atEnd && !isLoading && lastRequestedCount != posts.size) {
                    lastRequestedCount = posts.size
                    onLoadMore()
                }
            }
    }

    Box(
        modifier = modifier
            .yutakaPanel()
            .padding(8.dp),
    ) {
        if (posts.isEmpty()) {
            GalleryEmptyState(
                isLoading = isLoading,
                status = status,
                starredOnly = starredOnly,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(185.dp),
                state = gridState,
                contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 14.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(posts, key = { it.id }) { post ->
                    YutakaGalleryCard(
                        post = post,
                        sourceTags = sourceTags,
                        starred = isStarred(post),
                        onToggleStar = { onToggleStar(post) },
                        onOpen = { onOpenPost(post) },
                        onOpenSource = { onOpenSource(post) },
                    )
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(gridState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                style = ScrollbarStyle(
                    minimalHeight = 28.dp,
                    thickness = 6.dp,
                    shape = RoundedCornerShape(3.dp),
                    hoverDurationMillis = 300,
                    unhoverColor = colors.panelContent.copy(alpha = 0.35f),
                    hoverColor = colors.panelContent.copy(alpha = 0.72f),
                ),
            )
        }

        if (isLoading && posts.isNotEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .size(26.dp),
                color = colors.backgroundStart,
            )
        }
    }
}

@Composable
private fun YutakaGalleryCard(
    post: Post,
    sourceTags: List<String>,
    starred: Boolean,
    onToggleStar: () -> Unit,
    onOpen: () -> Unit,
    onOpenSource: () -> Unit,
) {
    val colors = YutakaTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val sourceTag = sourceTags.firstOrNull { it in post.tags }

    Box(
        modifier = Modifier
            .aspectRatio(0.74f)
            .yutakaItemBackground(highlighted = hovered)
            .hoverable(interaction)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClickLabel = "Open ${post.title}", onClick = onOpen)
            .padding(5.dp),
    ) {
        AsyncImage(
            model = post.smallUri,
            contentDescription = post.title,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Res.drawable._894jfct80ip51),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(7.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            YutakaBadge(
                text = ratingLabel(post.rating),
                background = ratingColor(post.rating),
                content = colors.headerOutline,
            )
            YutakaBadge(
                text = "score ${post.score}",
                background = colors.headerEnd,
                content = colors.headerContent,
            )
        }

        if (hovered || starred) {
            YutakaStarButton(
                starred = starred,
                onClick = onToggleStar,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
            )
        }

        if (hovered) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, colors.headerOutline.copy(alpha = 0.94f))
                        )
                    )
                    .padding(start = 9.dp, end = 9.dp, top = 18.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                YutakaOutlinedText(
                    text = post.title.ifBlank { "Post ${post.id}" },
                    fillColor = colors.itemContent,
                    outlineColor = colors.headerOutline,
                    fontSize = 13.sp,
                    outlineSize = 3.5f,
                    maxLines = 1,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YutakaOutlinedText(
                        text = post.author.ifBlank { sourceTag ?: post.host },
                        fillColor = colors.itemContentSecondary,
                        outlineColor = colors.headerOutline,
                        fontSize = 11.sp,
                        outlineSize = 3f,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )
                    YutakaButton(
                        text = "source",
                        onClick = onOpenSource,
                        primary = false,
                        fontSize = 11,
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryEmptyState(
    isLoading: Boolean,
    status: LoadingStatus,
    starredOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            YutakaOutlinedText(
                text = when {
                    starredOnly -> "No saved posts yet"
                    isLoading -> "Loading images..."
                    status == LoadingStatus.FAIL -> "Could not reach the booru"
                    else -> "No images for these tags"
                },
                fillColor = colors.panelContent,
                outlineColor = colors.headerOutline,
                fontSize = 20.sp,
                outlineSize = 4.5f,
                maxLines = 1,
            )
            YutakaOutlinedText(
                text = when {
                    starredOnly -> "Hover an image and tap the star to save it here."
                    status == LoadingStatus.FAIL -> "Check the connection and press Refresh."
                    else -> "Try lucky_star or a character tag."
                },
                fillColor = colors.panelContentSecondary,
                outlineColor = colors.headerOutline,
                fontSize = 13.sp,
                outlineSize = 3f,
                maxLines = 2,
            )
        }
    }
}

private fun ratingLabel(rating: String): String = when (rating.lowercase().take(1)) {
    "g" -> "safe"
    "s" -> "sensitive"
    "q" -> "questionable"
    "e" -> "explicit"
    else -> rating.ifBlank { "?" }
}

private fun ratingColor(rating: String): Color = when (rating.lowercase().take(1)) {
    "g" -> Color(0xFFBDEBB0)
    "s" -> Color(0xFFF2C14E)
    else -> Color(0xFFFFC7C0)
}
