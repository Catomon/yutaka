package io.github.catomon.yutaka.data.local.platform

import io.github.catomon.yutaka.domain.ImageSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageSaverImpl : ImageSaver {
    override suspend fun saveImage(bytes: ByteArray, fileName: String, fileExt: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userHome = System.getProperty("user.home") ?: return@withContext false
            val desktop = File(userHome, "Desktop")
            if (!desktop.exists()) desktop.mkdirs()
            val file = File(desktop, "$fileName.$fileExt")

            file.writeBytes(bytes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isDownloaded(fileName: String): Boolean {
        val userHome = System.getProperty("user.home") ?: return false
        val desktop = File(userHome, "Desktop")
        return File(desktop, fileName).exists()
    }
}