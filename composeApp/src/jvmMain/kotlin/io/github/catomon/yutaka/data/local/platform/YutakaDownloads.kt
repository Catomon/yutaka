package io.github.catomon.yutaka.data.local.platform

import java.io.File

/** Folder where the desktop app saves downloaded images. */
val yutakaDownloadsFolder: File
    get() {
        val home = System.getProperty("user.home") ?: "."
        return File(File(home, "Downloads"), "Yutaka")
    }

fun ensureYutakaDownloadsFolder(): File = yutakaDownloadsFolder.apply {
    if (!exists()) mkdirs()
}
