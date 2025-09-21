package io.github.catomon.yutaka.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.catomon.yutaka.domain.Post
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources._894jfct80ip51

@Composable
fun Posts(
    posts: List<Post>,
    onOpenPost: (Post) -> Unit,
    modifier: Modifier,
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(posts) {
        gridState.animateScrollToItem(0)
    }

    if (posts.isEmpty())
        Text("Empty")
    else
        LazyVerticalGrid(GridCells.Adaptive(150.dp), modifier, state = gridState) {
            items(posts.size, key = { posts[it].id }) {
                val post = posts[it]

                Box(Modifier.animateItem().size(150.dp, 200.dp).padding(2.dp), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = post.smallUri,
                        contentDescription = null,
                        modifier = Modifier.clip(RoundedCornerShape(3.dp)).clickable { onOpenPost(post) },
                        placeholder = painterResource(Res.drawable._894jfct80ip51),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                }
            }
        }
}