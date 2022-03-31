package com.codequark.yelp.managers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.NonNull
import androidx.annotation.StringRes

@Suppress("unused")
class PreferenceManager private constructor() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var resources: Resources

    companion object {
        @Volatile
        private var instance: PreferenceManager? = null

        fun getInstance(): PreferenceManager = instance ?: synchronized(this) {
            instance ?: PreferenceManager().also {
                instance = it
            }
        }
    }

    fun initialize(@NonNull application: Application, @NonNull preferencesName: String) {
        this.resources = application.applicationContext.resources
        this.preferences = application.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    init {
        if(instance != null) {
            throw RuntimeException("Use getInstance() method to get instance the single instance of this class.")
        }
    }

    fun removeAll() {
        this.editor = preferences.edit()
        this.editor.clear()
        this.editor.apply()
    }

    fun cleanKey(@StringRes resourceKey: Int) {
        this.editor = preferences.edit()
        this.editor.putString(resources.getString(resourceKey), "")
        this.editor.apply()
    }

    fun set(@StringRes resourceKey: Int, @NonNull value: String) {
        this.editor = preferences.edit()
        this.editor.putString(resources.getString(resourceKey), value)
        this.editor.apply()
    }

    @NonNull
    fun getString(@NonNull resourceKey: Int): String {
        return preferences.getString(resources.getString(resourceKey), "") ?: return ""
    }

    fun set(@NonNull resourceKey: Int, @NonNull value: Int) {
        this.editor = preferences.edit()
        this.editor.putInt(resources.getString(resourceKey), value)
        this.editor.apply()
    }

    @NonNull
    fun getInteger(@StringRes resourceKey: Int): Int {
        return preferences.getInt(resources.getString(resourceKey), 0)
    }

    fun set(@StringRes resourceKey: Int, @NonNull value: Boolean) {
        this.editor = preferences.edit()
        this.editor.putBoolean(resources.getString(resourceKey), value)
        this.editor.apply()
    }

    @NonNull
    fun getBoolean(key: Int): Boolean {
        return preferences.getBoolean(resources.getString(key), false)
    }
}