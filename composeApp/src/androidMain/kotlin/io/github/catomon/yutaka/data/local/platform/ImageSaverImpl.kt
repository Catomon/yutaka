package io.github.catomon.yutaka.data.local.platform

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.github.catomon.yutaka.domain.ImageSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageSaverImpl(private val context: Context) : ImageSaver {
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun saveImage(bytes: ByteArray, fileName: String, fileExt: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.$fileExt")
                put(MediaStore.Images.Media.MIME_TYPE, "image/$fileExt")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Yutaka")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val resolver = context.contentResolver
            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = resolver.insert(collection, values) ?: return@withContext false

            resolver.openOutputStream(uri).use { it?.write(bytes) }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isDownloaded(fileName: String): Boolean {
        val resolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ? AND ${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, "${Environment.DIRECTORY_PICTURES}/Yutaka/")
        resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            return cursor.count > 0
        }
        return false
    }
}