package io.github.catomon.yutaka.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.catomon.yutaka.data.mappers.toDomain
import io.github.catomon.yutaka.data.remote.dto.DanbooruApi
import io.github.catomon.yutaka.data.remote.dto.PostItemDto
import io.github.catomon.yutaka.domain.Post
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import java.net.SocketException
import java.util.concurrent.CancellationException

@Composable
fun MainScreen(api: DanbooruApi) {
    var page by remember { mutableStateOf(1) }
    var posts by remember { mutableStateOf(emptyList<Post>()) }
    var tries by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }
    var viewPost by remember { mutableStateOf<Post?>(null) }

    LoadPostsEffect(
        api = api, tries = tries,
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

@Composable
fun LoadPostsEffect(
    api: DanbooruApi,
    tries: Int,
    onPostsLoaded: (List<Post>) -> Unit,
    onStatusUpdate: (String) -> Unit,
    page: Int,
) {
    val json = remember {
        Json { ignoreUnknownKeys = true; isLenient = true }
    }

    LaunchedEffect(tries, page) {
        val posts: MutableList<PostItemDto> = mutableListOf()
        try {
            val jsonArray = api.getPosts(tags = "lucky_star", limit = 40, page = page).jsonArray
            for (jsonElement in jsonArray) {
                try {
                    val post = json.decodeFromJsonElement(PostItemDto.serializer(), jsonElement)
                    if (post.rating in listOf("g", "s")) { //g, s, q, e
                        posts.add(post)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            onStatusUpdate("Success")
            onPostsLoaded(posts.mapNotNull { if (it.score >= 4) it.toDomain() else null })
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