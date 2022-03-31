package com.codequark.yelp.models.location

import androidx.annotation.NonNull

data class UserLocation(
    @NonNull
    val latitude: Double,

    @NonNull
    val longitude: Double
)