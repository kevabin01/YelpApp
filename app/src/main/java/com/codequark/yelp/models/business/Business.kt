package com.codequark.yelp.models.business

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class Business(
    @NonNull
    @SerializedName("id")
    val id: String,

    @NonNull
    @SerializedName("alias")
    val alias: String,

    @NonNull
    @SerializedName("name")
    val name: String,

    @NonNull
    @SerializedName("image_url")
    val imageUrl: String,

    @NonNull
    @SerializedName("is_closed")
    val isClosed: Boolean,

    @NonNull
    @SerializedName("url")
    val url: String,

    @NonNull
    @SerializedName("review_count")
    val reviewCount: Int,

    @NonNull
    @SerializedName("categories")
    val categories: List<Category>,

    @NonNull
    @SerializedName("rating")
    val rating: Double,

    @NonNull
    @SerializedName("coordinates")
    val coordinates: Coordinate,

    @NonNull
    @SerializedName("price")
    val price: String?,

    @NonNull
    @SerializedName("location")
    val location: Location,

    @NonNull
    @SerializedName("phone")
    val phone: String,

    @NonNull
    @SerializedName("display_phone")
    val displayPhone: String,

    @NonNull
    @SerializedName("distance")
    val distance: Double
)