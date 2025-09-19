package io.github.catomon.yutaka.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.domain.PostRepository
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.last
import java.net.SocketException
import java.util.concurrent.CancellationException

@Composable
fun MainScreen(postRepo: PostRepository) {
    var page by remember { mutableStateOf(0) }
    var posts by remember { mutableStateOf(emptyList<Post>()) }
    var tries by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }
    var viewPost by remember { mutableStateOf<Post?>(null) }

    LoadPostsEffect(
        postRepo = postRepo, tries = tries,
        onPostsLoaded = { posts = it },
        onStatusUpdate = { status = it },
        page = page
    )

    AnimatedContent(viewPost) {
        when (it) {
            null ->
                PostsScreen(
                    posts = posts,
                    onRefresh = {
                        tries++
                    },
                    onOpenPost = {
                        viewPost = it
                    },
                    onClosePost = {
                        viewPost = null
                    },
                    page = page,
                    pages = Int.MAX_VALUE,
                    onPage = {
                        page = it
                    },
                )

            else ->
                PostViewScreen(it!!, {
                    viewPost = null
                }, Modifier.fillMaxSize().background(Color.Black))
        }
    }

}

const val MIN_SCORE = 0
const val PAGE_LIMIT = 40

@Composable
fun LoadPostsEffect(
    postRepo: PostRepository,
    tries: Int,
    onPostsLoaded: (List<Post>) -> Unit,
    onStatusUpdate: (String) -> Unit,
    page: Int,
) {
    var posts by remember { mutableStateOf(emptyList<Post>()) }

    LaunchedEffect(Unit) {
        posts = postRepo.getLocalPosts().last() //todo
        onPostsLoaded(posts.filter { it.score >= MIN_SCORE }) //todo
        onStatusUpdate("Cache loaded")
    }

    LaunchedEffect(tries, page) {
        try {
            posts = postRepo.getRemotePosts(tags = "lucky_star", limit = PAGE_LIMIT, page = page) //todo
            onPostsLoaded(posts.filter { it.score >= MIN_SCORE }) //todo
            postRepo.updateLocalPosts(posts)
            onStatusUpdate("Success")
        } catch (e: HttpRequestTimeoutException) {
            onStatusUpdate("Request Timeout")
            e.printStackTrace()
        } catch (e: SocketException) {
            onStatusUpdate(e.message ?: "Unknown error")
            e.printStackTrace()
        } catch (_: CancellationException) {

        } catch (e: Exception) {
            onStatusUpdate(e.message ?: "Unknown error")
            e.printStackTrace()
        }
    }
}