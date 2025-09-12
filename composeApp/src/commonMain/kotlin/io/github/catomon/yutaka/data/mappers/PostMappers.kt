package io.github.catomon.yutaka.data.mappers

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
    tags = tagString.split(" "),
    fileExt = fileExt
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
    tags = tags.split(" "),
    fileExt = fileUrl.split(".").last()
)