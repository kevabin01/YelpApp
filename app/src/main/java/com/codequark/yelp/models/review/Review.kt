package com.codequark.yelp.models.review

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Review(
    @NonNull
    @SerializedName("id")
    val id: String,

    @NonNull
    @SerializedName("url")
    val url: String,

    @NonNull
    @SerializedName("text")
    val text: String,

    @NonNull
    @SerializedName("rating")
    val rating: Int,

    @NonNull
    @SerializedName("time_created")
    val timeCreated: String,

    @NonNull
    @SerializedName("user")
    val user: User
)