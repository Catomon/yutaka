package io.github.catomon.yutaka.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SafebooruPostDto(
    @SerialName("preview_url") val previewUrl: String,
    @SerialName("sample_url") val sampleUrl: String,
    @SerialName("file_url") val fileUrl: String,
    val directory: String,
    val hash: String,
    val width: Int,
    val height: Int,
    val id: Long,
    val image: String,
    val change: Long,
    val owner: String,
    @SerialName("parent_id") val parentId: Long,
    val rating: String, // s q e
    val sample: Boolean,
    @SerialName("sample_height") val sampleHeight: Int,
    @SerialName("sample_width") val sampleWidth: Int,
    val score: Int? = null,
    val tags: String,
    val source: String,
    val status: String,
    @SerialName("has_notes") val hasNotes: Boolean,
    @SerialName("comment_count") val commentCount: Int
)