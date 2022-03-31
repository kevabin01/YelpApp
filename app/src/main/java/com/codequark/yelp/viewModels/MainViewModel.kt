package com.codequark.yelp.viewModels

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.navigation.ui.AppBarConfiguration
import com.codequark.yelp.models.business.DetailItem
import com.codequark.yelp.models.location.UserLocation
import com.codequark.yelp.models.review.Review
import com.codequark.yelp.room.models.LocalBusiness

class MainViewModel(application: Application): LoginViewModel(application) {
    @NonNull
    val destination: LiveData<Int> = repository.getDestination()

    @NonNull
    val mainState: LiveData<Int> = repository.getMainState()

    @NonNull
    val userLocation: LiveData<UserLocation> = repository.getUserLocation()

    @NonNull
    val navConfiguration: AppBarConfiguration = repository.navConfiguration

    @NonNull
    private val handler = Handler(Looper.getMainLooper())

    @NonNull
    fun getQuery(): String {
        return repository.getQuery()
    }

    fun setDestination(@IdRes destination: Int) {
        this.repository.setDestination(destination)
    }

    fun setQuery(@NonNull query: String) {
        this.repository.setQuery(query)

        if(query.isEmpty()) {
            return
        }

        this.handler.removeCallbacksAndMessages(null)
        this.handler.postDelayed({
            val userLocation: UserLocation = this.userLocation.value ?: return@postDelayed
            val latitude = userLocation.latitude
            val longitude = userLocation.longitude

            requestSearch(query, latitude, longitude)
        }, 1000)
    }

    fun getInfoDetail(@NonNull localBusiness: LocalBusiness): List<DetailItem> {
        return repository.getInfoDetail(localBusiness)
    }

    fun getInfoLocation(@NonNull localBusiness: LocalBusiness): List<DetailItem> {
        return repository.getInfoLocation(localBusiness)
    }

    fun getInfoReviews(@NonNull reviews: List<Review>): List<DetailItem> {
        return repository.getInfoReviews(reviews)
    }

    fun getLastLocation() {
        val application: Application = getApplication()
        val context: Context = application.applicationContext
        this.repository.getLastLocation(context)
    }

    @NonNull
    fun checkPermissions(): Boolean {
        val application: Application = getApplication()
        val context: Context = application.applicationContext
        return repository.checkPermissions(context)
    }
}