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
import io.github.catomon.yutaka.domain.BooruProvider
import io.github.catomon.yutaka.domain.Post
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.net.SocketException
import java.util.concurrent.CancellationException

@Composable
fun MainScreen(booru: BooruProvider) {
    var page by remember { mutableStateOf(1) }
    var posts by remember { mutableStateOf(emptyList<Post>()) }
    var tries by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }
    var viewPost by remember { mutableStateOf<Post?>(null) }

    LoadPostsEffect(
        booru = booru, tries = tries,
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
    booru: BooruProvider,
    tries: Int,
    onPostsLoaded: (List<Post>) -> Unit,
    onStatusUpdate: (String) -> Unit,
    page: Int,
) {
    LaunchedEffect(tries, page) {
        try {
            val posts = booru.getPosts(tags = "lucky_star", limit = PAGE_LIMIT, page = page)
            onStatusUpdate("Success")
            onPostsLoaded(posts.filter { it.score >= MIN_SCORE })
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