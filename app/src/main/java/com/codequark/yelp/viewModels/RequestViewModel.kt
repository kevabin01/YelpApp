package com.codequark.yelp.viewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.codequark.yelp.models.business.Business
import com.codequark.yelp.models.review.Review
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.utils.Constants

open class RequestViewModel(application: Application): DaoViewModel(application) {
    @NonNull
    private val updating = repository.getUpdating()

    @NonNull
    private val exception = repository.getException()

    @NonNull
    private val connection = repository.getConnection()

    @NonNull
    val localBusiness: LiveData<LocalBusiness> = repository.getLocalBusiness()

    @NonNull
    val businesses: LiveData<List<Business>> = repository.getBusinesses()

    @NonNull
    val reviews: LiveData<List<Review>> = repository.getReviews()

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
        this.repository.setUpdating(updating)
    }

    fun setException(@NonNull ex: Exception) {
        this.repository.setException(ex)
    }

    fun setConnection(@NonNull connection: Boolean) {
        this.repository.setConnection(connection)
    }

    fun setReviews(@NonNull reviews: List<Review>) {
        this.repository.setReviews(reviews)
    }

    fun setModel(@NonNull model: LocalBusiness) {
        this.repository.setLocalBusiness(model)
    }

    fun setModel(@NonNull model: Business) {
        val localBusiness = repository.fromBusinessToLocalBusiness(model)
        this.repository.replace(localBusiness)
        this.repository.setLocalBusiness(localBusiness)
    }

    fun requestReviews(@NonNull alias: String) {
        val params: HashMap<String, Any> = HashMap()

        params[Constants.JsonConstants.alias] = alias

        this.repository.requestReviews(params)
    }

    fun requestSearch(@NonNull query: String, @NonNull latitude: Double, @NonNull longitude: Double) {
        val params: HashMap<String, Any> = HashMap()

        params[Constants.JsonConstants.query] = query
        params[Constants.JsonConstants.latitude] = latitude
        params[Constants.JsonConstants.longitude] = longitude

        this.repository.requestSearch(params)
    }
}