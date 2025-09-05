package io.github.catomon.yutaka.data.remote.dto

import de.jensklingenberg.ktorfit.http.GET
import kotlinx.serialization.json.JsonElement

interface DanbooruApi {
    @GET("/posts.json/")
    suspend fun getPosts() : JsonElement //cant use List<PostItemDto> cus can be different result body

    //@GET("/books/v1/volumes/{googleId}")
    //	public Call<BookDetailsRespond> getBookDetails(@Path("googleId") String googleId,
    //							@Query("key") String key);a

}