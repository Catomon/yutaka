package io.github.catomon.yutaka.data.local.platform

import androidx.room.Room
import io.github.catomon.yutaka.appContext
import io.github.catomon.yutaka.data.local.PostDatabase

actual fun createPostDatabase(): PostDatabase {
    val room = Room.databaseBuilder<PostDatabase>(
        appContext!!,
        "posts_cache.db"
    )
        .fallbackToDestructiveMigration(false)
        .build()
    return room
}