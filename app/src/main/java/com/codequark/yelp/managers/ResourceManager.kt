package com.codequark.yelp.managers

import android.app.Application
import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ResourceManager private constructor() {
    lateinit var application: Application

    companion object {
        @Volatile
        private var instance: ResourceManager? = null

        fun getInstance(): ResourceManager = instance ?: synchronized(this) {
            instance ?: ResourceManager().also {
                instance = it
            }
        }
    }

    init {
        if(instance != null) {
            throw RuntimeException("Use getInstance() method to get instance the single instance of this class.")
        }
    }

    fun initialize(@NonNull application: Application) {
        this.application = application
    }

    @NonNull
    fun getString(@StringRes id: Int): String {
        return getContext().resources.getString(id)
    }

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(getContext(), id)
    }

    @NonNull
    fun getContext(): Context {
        return application.applicationContext
    }
}