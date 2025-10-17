package io.github.catomon.yutaka.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.ui.util.LoadingStatus
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopMainScreen(viewModel: MainViewModel = koinViewModel<MainViewModel>(), modifier: Modifier = Modifier) {
    val viewPost = viewModel.viewPost

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(
            Modifier.matchParentSize().padding(top = TopBarDefaults.HEIGHT.dp),
            contentAlignment = Alignment.Center
        ) {
            Posts(
                posts = viewModel.posts,
                onOpenPost = viewModel::openPost,
                modifier = Modifier.fillMaxSize()
            )

            AnimatedVisibility(viewPost != null, modifier = Modifier.fillMaxSize().clickable(false) { }) {
                //for the exit animation to work
                var post: Post? by remember { mutableStateOf<Post?>(null) }
                LaunchedEffect(viewPost) {
                    if (viewPost != null)
                        post = viewPost
                }
                post?.let {
                    PostViewScreen(
                        it,
                        onLoadState = {
                            viewModel.onPostViewLoadStatus(
                                when (it) {
                                    AsyncImagePainter.State.Empty -> LoadingStatus.LOADING
                                    is AsyncImagePainter.State.Error -> LoadingStatus.FAIL
                                    is AsyncImagePainter.State.Loading -> LoadingStatus.LOADING
                                    is AsyncImagePainter.State.Success -> LoadingStatus.SUCCESS
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize().background(Color.Black)
                    )
                }
            }
        }

        DesktopActionBar(viewModel, {
            //todo
        })
    }
}
