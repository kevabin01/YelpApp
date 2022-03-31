package com.codequark.yelp.retrofit.services

import androidx.annotation.NonNull
import com.codequark.yelp.retrofit.models.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/v3/businesses/search")
    suspend fun request(
        @NonNull
        @Query("term") query: String,

        @NonNull
        @Query("latitude") latitude: Double,

        @NonNull
        @Query("longitude") longitude: Double
    ): Response<SearchResult>
}