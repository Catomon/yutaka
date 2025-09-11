package io.github.catomon.yutaka.data.mappers

import io.github.catomon.yutaka.data.remote.dto.PostItemDto
import io.github.catomon.yutaka.domain.Post

fun PostItemDto.toDomain() = Post(
    id = id.toString(),
    originalUri = fileUrl,
    largeUri = largeFileUrl,
    smallUri = previewFileUrl,
    title = tagStringCharacter,
    details = tagStringGeneral,
    author = tagStringArtist,
    source = source,
    tags = tagString.split(" "),
    fileExt = fileExt
)