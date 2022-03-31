package com.codequark.yelp.models.review

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class User(
    @NonNull
    @SerializedName("id")
    val id: String,

    @NonNull
    @SerializedName("profile_url")
    val profileUrl: String,

    @NonNull
    @SerializedName("image_url")
    val imageUrl: String,

    @NonNull
    @SerializedName("name")
    val name: String
)