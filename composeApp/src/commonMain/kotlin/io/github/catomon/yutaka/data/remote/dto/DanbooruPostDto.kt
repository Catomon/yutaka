package io.github.catomon.yutaka.data.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.serialization.SerialName

@OptIn(ExperimentalTime::class)
@Serializable
data class DanbooruPostDto(
    @SerialName("id") val id: Int,
    @SerialName("uploader_id") val uploaderId: Int,
    @SerialName("approver_id") val approverId: Int?,
    @SerialName("tag_string") val tagString: String,
    @SerialName("tag_string_general") val tagStringGeneral: String,
    @SerialName("tag_string_artist") val tagStringArtist: String,
    @SerialName("tag_string_copyright") val tagStringCopyright: String,
    @SerialName("tag_string_character") val tagStringCharacter: String,
    @SerialName("tag_string_meta") val tagStringMeta: String,
    @SerialName("rating") val rating: String?, // g, s, q, e or null
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("source") val source: String,
    @SerialName("md5") val md5: String,
    @SerialName("file_url") val fileUrl: String,
    @SerialName("large_file_url") val largeFileUrl: String,
    @SerialName("preview_file_url") val previewFileUrl: String,
    @SerialName("file_ext") val fileExt: String,
    @SerialName("file_size") val fileSize: Int,
    @SerialName("image_width") val imageWidth: Int,
    @SerialName("image_height") val imageHeight: Int,
    @SerialName("score") val score: Int,
    @SerialName("fav_count") val favCount: Int,
    @SerialName("tag_count_general") val tagCountGeneral: Int,
    @SerialName("tag_count_artist") val tagCountArtist: Int,
    @SerialName("tag_count_copyright") val tagCountCopyright: Int,
    @SerialName("tag_count_character") val tagCountCharacter: Int,
    @SerialName("tag_count_meta") val tagCountMeta: Int,
    @SerialName("last_comment_bumped_at") @Contextual val lastCommentBumpedAt: Instant?,
    @SerialName("last_noted_at") @Contextual val lastNotedAt: Instant?,
    @SerialName("has_children") val hasChildren: Boolean,
    @SerialName("created_at") @Contextual val createdAt: Instant,
    @SerialName("updated_at") @Contextual val updatedAt: Instant
)