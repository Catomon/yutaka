package io.github.catomon.yutaka.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.catomon.yutaka.domain.ImageDownloader
import io.github.catomon.yutaka.domain.ImageSaver
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.domain.PostRepository
import io.github.catomon.yutaka.ui.util.LoadingStatus
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketException
import kotlin.coroutines.cancellation.CancellationException

class MainViewModel(private val postRepo: PostRepository, private val imageDownloader: ImageDownloader) : ViewModel() {
    companion object {
        private const val MIN_SCORE = 0
        private const val PAGE_LIMIT = 40
    }

    var page by mutableStateOf(0)
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

    fun isDownloaded(post: Post) = imageDownloader.isDownloaded(post)

    suspend fun downloadPost(post: Post, progress: (Int) -> Unit): Boolean = imageDownloader.downloadPost(post, progress)

    fun onPostViewLoadStatus(status: LoadingStatus) {
        viewPostStatus = status
    }
}