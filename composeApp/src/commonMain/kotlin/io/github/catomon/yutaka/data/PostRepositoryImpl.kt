package io.github.catomon.yutaka.data

import io.github.catomon.yutaka.data.local.PostDatabase
import io.github.catomon.yutaka.data.mappers.toDomain
import io.github.catomon.yutaka.data.mappers.toEntity
import io.github.catomon.yutaka.domain.BooruProvider
import io.github.catomon.yutaka.domain.Post
import io.github.catomon.yutaka.domain.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PostRepositoryImpl( //TODO GET LOCAL POSTS BY PAGE AND TAGS (RN ONLY LAST LOADED PAGE SHOWN)
    val database: PostDatabase,
    val booruProvider: BooruProvider,
) : PostRepository {
    override fun getPosts(tags: String, limit: Int, page: Int): Flow<List<Post>> {
        return flow<List<Post>> {
            emit(database.postDao().getListOfAll().map { it.toDomain() })
            emit(booruProvider.getPosts(tags = tags, limit = limit, page = page))
        }
    }

    override fun getLocalPosts(): Flow<List<Post>> = flow {
        val localPosts = withContext(Dispatchers.IO) {
            database.postDao().getListOfAll().map { it.toDomain() }
        }

        emit(localPosts)
    }

    override suspend fun getRemotePosts(tags: String, limit: Int, page: Int): List<Post> {
        return booruProvider.getPosts(tags = tags, limit = limit, page = page)
    }

    override suspend fun updateLocalPosts(posts: List<Post>) {
        database.postDao().deleteAll()
        database.postDao().insertAll(posts.map { it.toEntity() })
    }

    override suspend fun deleteLocalPosts() {
        database.postDao().deleteAll()
    }
}