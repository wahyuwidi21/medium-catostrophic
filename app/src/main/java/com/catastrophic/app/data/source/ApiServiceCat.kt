package com.catastrophic.app.data.source

import com.catastrophic.app.data.model.Cat
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceCat {
    @GET("v1/images/search")
    fun getCatsImageAsync(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20,
        @Query("mime_types") mimeTypes: String = "png",
        @Query("order") order: String = "Desc"
    ): Deferred<List<Cat>>

}