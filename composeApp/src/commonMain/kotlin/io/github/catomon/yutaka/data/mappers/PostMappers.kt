package io.github.catomon.yutaka.data.mappers

import io.github.catomon.yutaka.data.local.PostEntity
import io.github.catomon.yutaka.data.remote.dto.DanbooruPostDto
import io.github.catomon.yutaka.data.remote.dto.SafebooruPostDto
import io.github.catomon.yutaka.domain.Post

fun DanbooruPostDto.toDomain() = Post(
    id = id.toString(),
    originalUri = fileUrl,
    largeUri = largeFileUrl,
    smallUri = previewFileUrl,
    title = tagStringCharacter,
    details = tagStringGeneral,
    author = tagStringArtist,
    source = source,
    score = score,
    rating = rating ?: "e",
    tags = tagString.split(" "),
    fileExt = fileExt,
    host = "danbooru.donmai.us"
)

fun SafebooruPostDto.toDomain() = Post(
    id = id.toString(),
    originalUri = fileUrl,
    largeUri = sampleUrl,
    smallUri = previewUrl,
    title = tags.split(" ").first(),
    details = tags,
    author = "",
    source = source,
    score = score ?: 0,
    rating = rating,
    tags = tags.split(" "),
    fileExt = fileUrl.split(".").last(),
    host = "safebooru.org"
)

fun Post.toEntity() = PostEntity(
    id = host + "+" + id,
    title = "",
    artist = "",
    smallUri = smallUri,
    largeUri = largeUri,
    originalUri = originalUri,
    postUrl = if (host == "safebooru") "https://safebooru.org/index.php?page=post&s=view&id=" + id else "https://danbooru.donmai.us/posts/" + id,
    source = source,
    score = score,
    rating = rating,
    tags = tags,
    fileExt = fileExt,
)

fun PostEntity.toDomain() = Post(
    id = id.split("+").getOrNull(1)
        ?: throw IllegalStateException("Entity ID must follow host+id(of the post on the host) pattern."),
    host = id.split("+").firstOrNull()
        ?: throw IllegalStateException("Entity ID must follow host+id(of the post on the host) pattern."),
    largeUri = largeUri,
    smallUri = smallUri,
    originalUri = originalUri,
    title = title,
    details = "",
    author = artist,
    source = source,
    score = score,
    rating = rating,
    tags = tags,
    fileExt = fileExt
)