package com.codequark.yelp.models.login

import androidx.annotation.NonNull

data class Login(
    @NonNull
    val firebaseId: String,

    @NonNull
    val email: String,

    @NonNull
    val password: String
) {
    constructor(): this("", "", "")

    constructor(
        @NonNull
        email: String,

        @NonNull
        password: String
    ): this("", email, password)
}