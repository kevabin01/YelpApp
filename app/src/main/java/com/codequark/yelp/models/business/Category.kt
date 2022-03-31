package com.codequark.yelp.models.business

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Category(
    @NonNull
    @SerializedName("alias")
    val alias: String,

    @NonNull
    @SerializedName("title")
    val title: String
)