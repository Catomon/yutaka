package io.github.catomon.yutaka.domain

interface ImageDownloader {
    suspend fun downloadPost(post: Post, progress: (Int) -> Unit): Boolean
    fun isDownloaded(post: Post): Boolean
}