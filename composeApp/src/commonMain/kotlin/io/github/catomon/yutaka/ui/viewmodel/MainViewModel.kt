package io.github.catomon.yutaka.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.domain.PostRepository
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.net.SocketException
import kotlin.coroutines.cancellation.CancellationException

class MainViewModel(private val postRepo: PostRepository) : ViewModel() {
    companion object {
        private const val MIN_SCORE = 0
        private const val PAGE_LIMIT = 40

        const val STATUS_SUCCESS = 0
        const val STATUS_FAIL = 1
        const val STATUS_LOADING = 2
    }

    var page by mutableStateOf(0)
        private set

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var status by mutableStateOf(STATUS_LOADING)
        private set

    var viewPost by mutableStateOf<Post?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    private var tries by mutableStateOf(0)

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
                status = STATUS_LOADING
                isLoading = true
                val remotePosts = postRepo.getRemotePosts(tags = "lucky_star", limit = PAGE_LIMIT, page = page)
                posts = remotePosts.filter { it.score >= MIN_SCORE }
                postRepo.updateLocalPosts(remotePosts)
                status = STATUS_SUCCESS
            } catch (e: HttpRequestTimeoutException) {
                status = STATUS_FAIL
                e.printStackTrace()
            } catch (e: SocketException) {
                status = STATUS_FAIL
                e.printStackTrace()
            } catch (_: CancellationException) {
                // ignore
            } catch (e: Exception) {
                status = STATUS_FAIL
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

    /** From 0 to whatever */
    fun changePage(newPage: Int) {
        if (newPage == page || newPage < 0) return
        page = newPage
        refresh()
    }
}