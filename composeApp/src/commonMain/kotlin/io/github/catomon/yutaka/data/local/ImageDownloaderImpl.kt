package io.github.catomon.yutaka.data.local

import io.github.catomon.yutaka.domain.ImageDownloader
import io.github.catomon.yutaka.domain.ImageSaver
import io.github.catomon.yutaka.domain.Post
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageDownloaderImpl(
    private val imageSaver: ImageSaver
) : ImageDownloader {

    private val client = HttpClient()

    override fun isDownloaded(post: Post) = imageSaver.isDownloaded("${post.fileName()}.${post.fileExt}")

    fun Post.fileName() = "$host$id"

    override suspend fun downloadPost(post: Post, progress: (Int) -> Unit): Boolean = withContext(Dispatchers.IO) {
        val fileName = post.fileName()
        val fileExt = post.fileExt

        if (isDownloaded(post)) return@withContext true

        try {
            var lastProgress = 0
            val response: HttpStatement = client.prepareGet(post.originalUri) {
                timeout {
                    requestTimeoutMillis = 600_000
                    socketTimeoutMillis = 120_000
                }
                onDownload { bytesDownloaded, contentLength ->
                    val currentProgress =
                        (bytesDownloaded.toFloat() / (contentLength ?: return@onDownload) * 100).toInt()
                    if (currentProgress > lastProgress) {
                        progress(currentProgress)
                        lastProgress = currentProgress
                    }
                }
            }
            response.execute { httpResponse ->
                imageSaver.saveImage(httpResponse.readRawBytes(), fileName, fileExt)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}