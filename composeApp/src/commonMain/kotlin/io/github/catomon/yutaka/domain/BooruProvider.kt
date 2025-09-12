package io.github.catomon.yutaka.domain

interface BooruProvider {
    val ratings: List<String>
        get() = listOf("g", "s")

    suspend fun getPosts(limit: Int, tags: String, page: Int = 1): List<Post>
}