package com.codequark.yelp.repositories

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.paging.PagingConfig
import com.codequark.yelp.viewModels.SecureLiveData

abstract class BaseRepository protected constructor() {
    @NonNull
    private val updating = SecureLiveData(false)

    @NonNull
    private val exception = SecureLiveData<Exception>(null)

    @NonNull
    private val connection = SecureLiveData(false)

    @NonNull
    protected val pagingConfig = PagingConfig(60)

    @NonNull
    fun getUpdating(): LiveData<Boolean> {
        return updating
    }

    @NonNull
    fun getException(): LiveData<Exception> {
        return exception
    }

    @NonNull
    fun getConnection(): LiveData<Boolean> {
        return connection
    }

    fun setUpdating(@NonNull updating: Boolean) {
        this.updating.value = updating
    }

    fun setException(@NonNull ex: Exception) {
        this.exception.value = ex
        this.exception.postValue(null)
    }

    fun setConnection(@NonNull connection: Boolean) {
        this.connection.value = connection
        this.connection.postValue(false)
    }

    @NonNull
    fun isUpdating(): Boolean {
        return updating.value
    }
}