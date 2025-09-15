package io.github.catomon.yutaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val smallUri: String,
    val largeUri: String,
    val originalUri: String,
    val postUrl: String? = null,
    val source: String,
    val score: Int,
    val rating: String,
    val tags: List<String>,
    val fileExt: String,
)