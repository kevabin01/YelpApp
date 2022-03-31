package com.codequark.yelp.models.business

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Coordinate(
    @NonNull
    @SerializedName("latitude")
    val latitude: Double,

    @NonNull
    @SerializedName("longitude")
    val longitude: Double
)