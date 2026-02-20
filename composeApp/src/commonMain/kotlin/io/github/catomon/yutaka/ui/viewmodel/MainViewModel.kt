package io.github.catomon.yutaka.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.catomon.yutaka.domain.ImageDownloader
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.domain.PostRepository
import io.github.catomon.yutaka.ui.util.LoadingStatus
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.net.SocketException
import kotlin.coroutines.cancellation.CancellationException

object WindowConfig {
    const val WIDTH = 800
    const val HEIGHT = 600

    var isTransparent = true

    val isTraySupported = androidx.compose.ui.window.isTraySupported
}

class MainViewModel(private val postRepo: PostRepository, private val imageDownloader: ImageDownloader) : ViewModel() {
    companion object {
        private const val MIN_SCORE = 0
        private const val PAGE_LIMIT = 40
    }

    var page by mutableIntStateOf(0)
        private set

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var status by mutableStateOf(LoadingStatus.LOADING)
        private set

    var viewPost by mutableStateOf<Post?>(null)
        private set

    var viewPostStatus by mutableStateOf(LoadingStatus.LOADING)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var isDownloading by mutableStateOf(false)
        private set
    var downloaded by mutableStateOf(false)
        private set
    var progress by mutableIntStateOf(0)
        private set

    val windowState = WindowState(width = WindowConfig.WIDTH.dp, height = WindowConfig.HEIGHT.dp)

    init {
        loadLocalPosts()
        loadRemotePosts()
    }

    private fun loadLocalPosts() {
        viewModelScope.launch {
            val localPosts = postRepo.getLocalPosts().last()
            posts = localPosts.filter { it.score >= MIN_SCORE }
            isLoading = false
        }
    }

    private var loadingJob: Job? = null
    private fun loadRemotePosts() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            try {
                status = LoadingStatus.LOADING
                isLoading = true
                val remotePosts = postRepo.getRemotePosts(tags = "lucky_star", limit = PAGE_LIMIT, page = page)
                posts = remotePosts.filter { it.score >= MIN_SCORE }
                postRepo.updateLocalPosts(remotePosts)
                status = LoadingStatus.SUCCESS
            } catch (e: HttpRequestTimeoutException) {
                status = LoadingStatus.FAIL
                e.printStackTrace()
            } catch (e: SocketException) {
                status = LoadingStatus.FAIL
                e.printStackTrace()
            } catch (_: CancellationException) {
                // ignore
            } catch (e: Exception) {
                status = LoadingStatus.FAIL
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun refresh() {
        loadRemotePosts()
    }

    fun openPost(post: Post) {
        viewPost = post
        downloaded = isDownloaded(post)
    }

    fun closePost() {
        viewPost = null
    }

    /** From 0 to <>< */
    fun changePage(newPage: Int) {
        if (newPage == page || newPage < 0) return
        page = newPage
        refresh()
    }

    fun nextPost() {
        viewPost?.let { curPost ->
            var index = posts.indexOf(curPost)
            if (index != -1) {
                index++
                if (index >= posts.size) {
                    changePage(page + 1)
                    loadingJob?.invokeOnCompletion {
                        openPost(posts.firstOrNull() ?: return@invokeOnCompletion)
                    }
                } else {
                    openPost(posts.getOrNull(index) ?: return)
                }
            }
        }
    }

    fun prevPost() {
        viewPost?.let { curPost ->
            var index = posts.indexOf(curPost)
            if (index != -1) {
                index--
                if (index < 0) {
                    changePage(page - 1)
                    loadingJob?.invokeOnCompletion {
                        openPost(posts.lastOrNull() ?: return@invokeOnCompletion)
                    }
                } else {
                    openPost(posts.getOrNull(index) ?: return)
                }
            }
        }
    }

    fun isDownloaded(post: Post) = imageDownloader.isDownloaded(post)

    fun downloadViewedPost() {
        CoroutineScope(Dispatchers.Main).launch {
            val post = viewPost ?: return@launch
            isDownloading = true
            downloaded = false
            val result = imageDownloader.downloadPost(post) {
                progress = it
            }
            downloaded = result
            isDownloading = false
        }
    }


    fun onPostViewLoadStatus(status: LoadingStatus) {
        viewPostStatus = status
    }
}