package io.github.catomon.yutaka.data.remote

import io.github.catomon.yutaka.data.mappers.toDomain
import io.github.catomon.yutaka.data.remote.dto.SafebooruPostDto
import io.github.catomon.yutaka.data.utils.json
import io.github.catomon.yutaka.domain.BooruProvider
import io.github.catomon.yutaka.domain.Post
import kotlinx.serialization.json.jsonArray

class SafebooruProvider(
    private val api: SafebooruApi
) : BooruProvider {
    override suspend fun getPosts(limit: Int, tags: String, page: Int): List<Post> {
        val jsonArray = api.getPosts(limit = limit, tags = tags).jsonArray
        val posts: MutableList<SafebooruPostDto> = mutableListOf()
        for (jsonElement in jsonArray) {
            try {
                val post = json.decodeFromJsonElement(SafebooruPostDto.serializer(), jsonElement)
                if (post.rating.take(1) in ratings) { //g, s, q, e
                    posts.add(post)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return posts.map { it.toDomain() }
    }
}