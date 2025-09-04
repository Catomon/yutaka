package io.github.catomon.yutaka.data.remote.dto

import io.github.catomon.yutaka.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class PostItemDto(
    val id: Int,
    val uploaderId: Int,
    val approverId: Int?,
    val tagString: String,
    val tagStringGeneral: String,
    val tagStringArtist: String,
    val tagStringCopyright: String,
    val tagStringCharacter: String,
    val tagStringMeta: String,
    val rating: String?, // g, s, q, e or null
    val parentId: Int?,
    val source: String,
    val md5: String,
    val fileUrl: String,
    val largeFileUrl: String,
    val previewFileUrl: String,
    val fileExt: String,
    val fileSize: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val score: Int,
    val favCount: Int,
    val tagCountGeneral: Int,
    val tagCountArtist: Int,
    val tagCountCopyright: Int,
    val tagCountCharacter: Int,
    val tagCountMeta: Int,
    @Serializable(InstantSerializer::class) val lastCommentBumpedAt: Instant?,
    @Serializable(InstantSerializer::class) val lastNotedAt: Instant?,
    val hasChildren: Boolean,
    @Serializable(InstantSerializer::class) val createdAt: Instant,
    @Serializable(InstantSerializer::class) val updatedAt: Instant
)