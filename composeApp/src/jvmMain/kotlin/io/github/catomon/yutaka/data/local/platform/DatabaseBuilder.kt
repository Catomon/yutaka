package io.github.catomon.yutaka.data.local.platform

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.catomon.yutaka.data.local.PostDatabase

actual fun createPostDatabase(): PostDatabase {
    return Room.databaseBuilder<PostDatabase>(
        userFolderPath + "posts_cache.db"
    ).setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}