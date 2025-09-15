package io.github.catomon.yutaka.domain

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(tags: String, limit: Int, page: Int): Flow<List<Post>>

    fun getLocalPosts(): Flow<List<Post>>

    suspend fun getRemotePosts(tags: String, limit: Int, page: Int): List<Post>

    suspend fun updateLocalPosts(posts: List<Post>)

    suspend fun deleteLocalPosts()
}