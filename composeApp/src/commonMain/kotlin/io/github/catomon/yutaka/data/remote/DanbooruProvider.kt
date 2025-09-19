package io.github.catomon.yutaka.data.remote

import io.github.catomon.yutaka.data.mappers.toDomain
import io.github.catomon.yutaka.data.remote.dto.DanbooruPostDto
import io.github.catomon.yutaka.data.utils.json
import io.github.catomon.yutaka.domain.BooruProvider
import io.github.catomon.yutaka.domain.Post
import kotlinx.serialization.json.jsonArray

class DanbooruProvider(
    private val api: DanbooruApi
) : BooruProvider {

    override val MAX_POSTS_LIMIT: Int = 200 //200 for posts.json, 1000 for everything else

    override suspend fun getPosts(limit: Int, tags: String, page: Int): List<Post> {
        val jsonArray = api.getPosts(limit = limit, tags = tags, page = page).jsonArray
        val posts: MutableList<DanbooruPostDto> = mutableListOf()
        for (jsonElement in jsonArray) {
            try {
                val post = json.decodeFromJsonElement(DanbooruPostDto.serializer(), jsonElement)
                if (post.rating in ratings) { //g, s, q, e
                    posts.add(post)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return posts.map { it.toDomain() }
    }
}