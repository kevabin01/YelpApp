package com.codequark.yelp.interfaces

import androidx.annotation.NonNull

interface SuccessListener<T> {
    fun onSuccess(@NonNull result: T)
}