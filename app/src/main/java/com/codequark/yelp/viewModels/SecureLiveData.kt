package com.codequark.yelp.viewModels

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData

class SecureLiveData<T>: MutableLiveData<T> {
    constructor(): super()

    constructor(@Nullable content: T?): super(content)

    @NonNull
    override fun getValue(): T {
        return super.getValue() ?: throw RuntimeException("Content of LiveData are Null")
    }
}