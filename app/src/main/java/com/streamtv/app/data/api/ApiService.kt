package com.streamtv.app.data.api

import com.streamtv.app.data.model.ApiResponse
import com.streamtv.app.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movies")
    suspend fun getMovies(
        @Header("x-api-key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50,
        @Query("search") search: String? = null
    ): ApiResponse<List<Movie>>

    @GET("movies/{id}")
    suspend fun getMovie(
        @Header("x-api-key") apiKey: String,
        @Path("id") id: String
    ): ApiResponse<Movie>
}
