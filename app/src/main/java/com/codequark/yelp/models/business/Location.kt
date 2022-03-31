package com.codequark.yelp.models.business

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class Location(
    @Nullable
    @SerializedName("address1")
    val address1: String?,

    @Nullable
    @SerializedName("address2")
    val address2: String?,

    @Nullable
    @SerializedName("address3")
    val address3: String?,

    @NonNull
    @SerializedName("city")
    val city: String,

    @NonNull
    @SerializedName("zip_code")
    val zipCode: String,

    @NonNull
    @SerializedName("country")
    val country: String,

    @NonNull
    @SerializedName("state")
    val state: String,

    @NonNull
    @SerializedName("display_address")
    val displayAddress: List<String>
)