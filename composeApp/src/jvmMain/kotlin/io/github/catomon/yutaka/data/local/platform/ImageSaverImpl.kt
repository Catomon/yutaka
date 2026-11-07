package io.github.catomon.yutaka.data.local.platform

import io.github.catomon.yutaka.domain.ImageSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageSaverImpl : ImageSaver {
    override suspend fun saveImage(bytes: ByteArray, fileName: String, fileExt: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val folder = ensureYutakaDownloadsFolder()
            val file = File(folder, "$fileName.$fileExt")

            file.writeBytes(bytes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isDownloaded(fileName: String): Boolean {
        return File(yutakaDownloadsFolder, fileName).exists()
    }
}
