package io.github.catomon.yutaka.domain

interface ImageSaver {
    suspend fun saveImage(bytes: ByteArray, fileName: String, fileExt: String): Boolean
    fun isDownloaded(fileName: String): Boolean
}