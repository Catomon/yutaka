package io.github.catomon.yutaka.data.remote

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.json.JsonElement

interface SafebooruApi {
    @GET("index.php?page=dapi&s=post&q=index&json=1")
    suspend fun getPosts(
        @Query("limit") limit: Int? = null,
        @Query("pid") pid: Int? = null,
        @Query("tags") tags: String? = null,
        @Query("cid") cid: Long? = null,
        @Query("id") id: Long? = null,
    ): JsonElement

    @GET("index.php?page=dapi&s=post&q=index&deleted=show&json=1")
    suspend fun getDeletedImages(
        @Query("last_id") lastId: Long? = null,
    ): JsonElement

    @GET("index.php?page=dapi&s=comment&q=index&json=1")
    suspend fun getComments(
        @Query("post_id") postId: Long? = null,
    ): JsonElement

    @GET("index.php?page=dapi&s=tag&q=index&json=1")
    suspend fun getTags(
        @Query("id") id: Long? = null,
        @Query("limit") limit: Int? = null,
    ): JsonElement
}