package com.codequark.yelp.interfaces

import androidx.annotation.NonNull

interface ErrorListener {
    fun onError(@NonNull ex: Exception)
}