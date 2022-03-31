package com.codequark.yelp.retrofit.services

import androidx.annotation.NonNull
import com.codequark.yelp.retrofit.models.ReviewResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewService {
    @GET("/v3/businesses/{alias}/reviews")
    suspend fun request(
        @NonNull
        @Path("alias") alias: String,
    ): Response<ReviewResult>
}