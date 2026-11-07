package io.github.catomon.yutaka.ui.v2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.ui.local_providers.LocalWindowManager
import io.github.catomon.yutaka.ui.v2.components.YutakaButton
import io.github.catomon.yutaka.ui.v2.components.YutakaHeader
import io.github.catomon.yutaka.ui.v2.components.YutakaMenuOverlay
import io.github.catomon.yutaka.ui.v2.components.YutakaOutlinedText
import io.github.catomon.yutaka.ui.v2.components.YutakaSearchField
import io.github.catomon.yutaka.ui.v2.components.YutakaSegmented
import io.github.catomon.yutaka.ui.v2.components.YutakaStarfall
import io.github.catomon.yutaka.ui.v2.components.yutakaWindowDrag
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.background

enum class GallerySort(val label: String) {
    DATE("Date"),
    SCORE("Score"),
    TITLE("Title"),
}

@Composable
fun YutakaV2Screen(
    viewModel: MainViewModel,
    window: java.awt.Window,
    modifier: Modifier = Modifier,
) {
    val colors = YutakaTheme.colors
    val uriHandler = LocalUriHandler.current
    val windowManager = LocalWindowManager.current
    val starredStore = remember { YutakaStarredStore() }

    var isMenuOpen by remember { mutableStateOf(false) }
    var sortMode by remember { mutableStateOf(GallerySort.DATE) }
    var safeOnly by remember { mutableStateOf(false) }
    var starredOnly by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf(viewModel.tags) }

    LaunchedEffect(viewModel.tags) {
        query = viewModel.tags
    }

    val basePosts = if (starredOnly) starredStore.posts else viewModel.posts
    val visiblePosts = remember(basePosts, sortMode, safeOnly) {
        val filtered = basePosts
            .asSequence()
            .filter { !safeOnly || it.rating.lowercase().startsWith("g") }
            .distinctBy { it.id }
            .toList()

        when (sortMode) {
            GallerySort.DATE -> filtered
            GallerySort.SCORE -> filtered.sortedWith(
                compareByDescending<Post> { it.score }.thenBy { it.title.lowercase() }
            )
            GallerySort.TITLE -> filtered.sortedBy { it.title.lowercase() }
        }
    }

    Box(
        modifier = modifier.background(colors.backgroundMid),
    ) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.12f,
            modifier = Modifier.matchParentSize(),
        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors.backgroundMid.copy(alpha = 0.82f),
                            colors.backgroundEnd.copy(alpha = 0.55f),
                        )
                    )
                )
        )

        YutakaStarfall(Modifier.matchParentSize())

        Column(Modifier.fillMaxSize()) {
            YutakaHeader(
                onOpenMenu = { isMenuOpen = true },
                onMinimize = { windowManager.minimize() },
                onMaximize = { windowManager.maximize() },
                onClose = { windowManager.closeWindow() },
                dragModifier = Modifier.yutakaWindowDrag(window),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(top = 10.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YutakaSearchField(
                    value = query,
                    onValueChange = { query = it },
                    onSubmit = { viewModel.applyTags(query) },
                    modifier = Modifier.weight(1f),
                )

                YutakaSegmented(
                    options = listOf(
                        GallerySort.DATE to "Date",
                        GallerySort.SCORE to "Score",
                        GallerySort.TITLE to "Title",
                    ),
                    selected = sortMode,
                    onSelect = { sortMode = it },
                )

                YutakaSegmented(
                    options = listOf(false to "All", true to "Safe"),
                    selected = safeOnly,
                    onSelect = { safeOnly = it },
                )

                YutakaButton(
                    text = "Refresh",
                    onClick = viewModel::reload,
                    fontSize = 13,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                YutakaSidebar(
                    viewModel = viewModel,
                    visibleCount = visiblePosts.size,
                    totalCount = basePosts.size,
                    starredCount = starredStore.posts.size,
                    starredOnly = starredOnly,
                    onToggleStarred = { starredOnly = !starredOnly },
                    onOpenDownloads = { openDownloadsFolder() },
                    modifier = Modifier
                        .width(230.dp)
                        .fillMaxHeight(),
                )

                YutakaGallery(
                    posts = visiblePosts,
                    isLoading = viewModel.isLoading && !starredOnly,
                    status = viewModel.status,
                    scrollResetKey = "$sortMode-$safeOnly-$starredOnly-${viewModel.tags}",
                    sourceTags = remember(viewModel.tags) {
                        viewModel.tags.split(" ").filter { it.isNotBlank() }
                    },
                    starredOnly = starredOnly,
                    isStarred = starredStore::isStarred,
                    onToggleStar = starredStore::toggle,
                    onOpenPost = viewModel::openPost,
                    onOpenSource = { uriHandler.openUri(it.sourceUrl()) },
                    onLoadMore = { if (!starredOnly) viewModel.loadMore() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }

        if (isMenuOpen) {
            YutakaMenuOverlay(
                onRefresh = viewModel::reload,
                onOpenDownloads = { openDownloadsFolder() },
                onOpenSafebooru = { uriHandler.openUri("https://safebooru.org/") },
                onOpenGitHub = { uriHandler.openUri("https://github.com/Catomon") },
                onExit = { windowManager.closeWindow() },
                onClose = { isMenuOpen = false },
            )
        }

        viewModel.viewPost?.let { post ->
            YutakaViewer(
                post = post,
                viewModel = viewModel,
                starred = starredStore.isStarred(post),
                onToggleStar = { starredStore.toggle(post) },
                onClose = viewModel::closePost,
                onOpenSource = { uriHandler.openUri(post.sourceUrl()) },
            )
        }
    }
}
