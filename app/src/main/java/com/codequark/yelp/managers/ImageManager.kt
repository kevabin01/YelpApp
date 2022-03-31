package com.codequark.yelp.managers

import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.codequark.yelp.R

@Suppress("unused")
class ImageManager {
    private var isDefaultOptions: Boolean = false

    private val defaultOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(Target.SIZE_ORIGINAL)
            .dontTransform()

    private lateinit var thumbRequest: RequestBuilder<Drawable>
    private lateinit var errorRequest: RequestBuilder<Drawable>

    companion object {
        var instance = ImageManager()
    }

    fun initialize(@NonNull application: Application) {
        this.errorRequest = Glide.with(application.applicationContext)
            .load(R.drawable.ic_error)
            .apply(getOptions())

        this.thumbRequest = Glide.with(application.applicationContext)
            .load(R.drawable.ic_loading)
            .apply(getOptions())
    }

    fun setImage(@NonNull url: String, @NonNull image: ImageView) {
        Glide.with(image)
            .load(url)
            .apply(getOptions())
            .thumbnail(thumbRequest)
            .error(errorRequest)
            .into(image)
    }

    @NonNull
    private fun getOptions(): RequestOptions {
        return defaultOptions
    }
}