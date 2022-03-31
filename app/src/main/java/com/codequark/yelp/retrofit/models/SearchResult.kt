package com.codequark.yelp.retrofit.models

import androidx.annotation.NonNull
import com.codequark.yelp.models.business.Business
import com.google.gson.annotations.SerializedName

data class SearchResult(
    @NonNull
    @SerializedName("businesses")
    val businesses: List<Business>,

    @NonNull
    @SerializedName("total")
    val total: Int
): Result()