package com.codequark.yelp.retrofit.models

import androidx.annotation.NonNull
import com.codequark.yelp.models.review.Review
import com.google.gson.annotations.SerializedName

data class ReviewResult(
    @NonNull
    @SerializedName("reviews")
    val reviews: List<Review>,

    @NonNull
    @SerializedName("total")
    val total: Int
): Result()