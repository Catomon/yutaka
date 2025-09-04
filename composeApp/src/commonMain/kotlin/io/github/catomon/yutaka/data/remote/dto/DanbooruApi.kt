package io.github.catomon.yutaka.data.remote.dto

import de.jensklingenberg.ktorfit.http.GET

interface DanbooruApi {
    @GET("/posts.json/")
    suspend fun getPosts() : String

    //@GET("/books/v1/volumes/{googleId}")
    //	public Call<BookDetailsRespond> getBookDetails(@Path("googleId") String googleId,
    //							@Query("key") String key);a

}