package com.codequark.yelp.application

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.codequark.yelp.managers.ImageManager
import com.codequark.yelp.managers.PreferenceManager
import com.codequark.yelp.managers.ResourceManager
import com.codequark.yelp.managers.WriteManager

open class YelpApp: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        ResourceManager.getInstance().initialize(this)
        PreferenceManager.getInstance().initialize(this, "YelpApp")
        ImageManager.instance.initialize(this)
        WriteManager.initProject(this, "CodeQuark", "YelpApp")
    }
}