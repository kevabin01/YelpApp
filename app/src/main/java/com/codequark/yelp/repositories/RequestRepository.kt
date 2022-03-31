package com.codequark.yelp.repositories

import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.codequark.yelp.interfaces.ErrorListener
import com.codequark.yelp.interfaces.NetworkListener
import com.codequark.yelp.interfaces.SuccessListener
import com.codequark.yelp.models.business.Business
import com.codequark.yelp.models.review.Review
import com.codequark.yelp.retrofit.Endpoints
import com.codequark.yelp.retrofit.models.Result
import com.codequark.yelp.retrofit.models.ReviewResult
import com.codequark.yelp.retrofit.models.SearchResult
import com.codequark.yelp.retrofit.requests.ReviewRequest
import com.codequark.yelp.retrofit.requests.SearchRequest
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.SecureLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class RequestRepository(@NonNull context: Context): DaoRepository(context) {
    @NonNull
    private val localBusiness: SecureLiveData<LocalBusiness> = SecureLiveData(null)

    @NonNull
    private val businesses: SecureLiveData<List<Business>> = SecureLiveData(null)

    @NonNull
    private val reviews: SecureLiveData<List<Review>> = SecureLiveData(null)

    @NonNull
    fun getLocalBusiness(): LiveData<LocalBusiness> = localBusiness

    @NonNull
    fun getBusinesses(): LiveData<List<Business>> = businesses

    @NonNull
    fun getReviews(): LiveData<List<Review>> = reviews

    fun setLocalBusiness(@NonNull localBusiness: LocalBusiness) {
        this.localBusiness.value = localBusiness
    }

    private fun setBusinesses(@NonNull businesses: List<Business>) {
        this.businesses.value = businesses
    }

    fun setReviews(@NonNull reviews: List<Review>) {
        this.reviews.value = reviews
    }

    @NonNull
    private val errorListener = object: ErrorListener {
        override fun onError(ex: Exception) {
            setUpdating(false)
            setException(ex)
        }
    }

    @NonNull
    private val networkListener: NetworkListener = object: NetworkListener {
        override fun onConnected() {
            setUpdating(true)
        }

        override fun onDisconnected() {
            setUpdating(false)
            setConnection(true)
        }
    }

    fun requestReviews(@NonNull params: HashMap<String, Any>) {
        val headers: HashMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer ${Endpoints.API_KEY}"

        val request = ReviewRequest(headers)

        request.setSuccessListener(object: SuccessListener<Result> {
            override fun onSuccess(result: Result) {
                setUpdating(false)

                when (result) {
                    is ReviewResult -> {
                        LogUtils.print("Success Search with response: " + result.total)

                        val list: List<Review> = result.reviews
                        setReviews(list)
                    }

                    else -> {
                        throw RuntimeException("Error, unknown model list response (-2)")
                    }
                }
            }
        })

        request.setErrorListener(errorListener)

        request.setNetworkListener(networkListener)

        @Suppress("EXPERIMENTAL_API_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            request.execute(params)
        }
    }

    fun requestSearch(@NonNull params: HashMap<String, Any>) {
        val headers: HashMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer ${Endpoints.API_KEY}"

        val request = SearchRequest(headers)

        request.setSuccessListener(object: SuccessListener<Result> {
            override fun onSuccess(result: Result) {
                setUpdating(false)

                when (result) {
                    is SearchResult -> {
                        LogUtils.print("Success Search with response: " + result.total)

                        val list: List<Business> = result.businesses
                        setBusinesses(list)
                    }

                    else -> {
                        throw RuntimeException("Error, unknown model list response (-2)")
                    }
                }
            }
        })

        request.setErrorListener(errorListener)

        request.setNetworkListener(networkListener)

        @Suppress("EXPERIMENTAL_API_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            request.execute(params)
        }
    }

    fun fromBusinessToLocalBusiness(@NonNull businesses: List<Business>): List<LocalBusiness> {
        val localBusiness = ArrayList<LocalBusiness>()

        businesses.forEach { business ->
            val location = business.location
            val address = location.address1 + " " + location.address2 + " " + location.address3

            var categories = ""
            business.categories.forEach {
                categories += "${it.title}\n"
            }

            categories = categories.replace("\n", ", ")
            categories = categories.substring(0, categories.length)
            categories = categories.dropLast(2)

            var displayAddress = ""
            business.location.displayAddress.forEach {
                displayAddress += if(displayAddress.isEmpty()) {
                    "${it}\n"
                } else {
                    "${it}\n"
                }
            }

            displayAddress = displayAddress.replace("\n", ", ")
            displayAddress = displayAddress.substring(0, displayAddress.length)
            displayAddress = displayAddress.dropLast(2)

            localBusiness.add(
                LocalBusiness(
                    business.id,
                    business.alias,
                    business.name,
                    business.imageUrl,
                    business.isClosed,
                    business.url,
                    business.reviewCount,
                    categories,
                    business.rating,
                    business.coordinates.latitude,
                    business.coordinates.longitude,
                    business.price,
                    address,
                    business.location.city,
                    business.location.zipCode,
                    business.location.country,
                    business.location.state,
                    displayAddress,
                    business.phone,
                    business.displayPhone,
                    business.distance
                )
            )
        }

        return localBusiness
    }

    fun fromBusinessToLocalBusiness(@NonNull model: Business): LocalBusiness {
        val coordinate = model.coordinates
        val latitude = coordinate.latitude
        val longitude = coordinate.longitude

        val location = model.location
        val address = location.address1 + " " + location.address2 + " " + location.address3
        val city = location.city
        val zipCode = location.zipCode
        val country = location.country
        val state = location.state

        var categories = ""
        model.categories.forEach{
            categories += "${it.title}\n"
        }

        categories = categories.replace("\n", ", ")
        categories = categories.substring(0, categories.length)
        categories = categories.dropLast(2)

        var displayAddress = ""
        model.location.displayAddress.forEach{
            displayAddress += if(displayAddress.isEmpty()) {
                "${it}\n"
            } else {
                "${it}\n"
            }
        }

        displayAddress = displayAddress.replace("\n", ", ")
        displayAddress = displayAddress.substring(0, displayAddress.length)
        displayAddress = displayAddress.dropLast(2)

        return LocalBusiness(
            model.id,
            model.alias,
            model.name,
            model.imageUrl,
            model.isClosed,
            model.url,
            model.reviewCount,
            categories,
            model.rating,
            latitude,
            longitude,
            model.price,
            address,
            city,
            zipCode,
            country,
            state,
            displayAddress,
            model.phone,
            model.displayPhone,
            model.distance
        )
    }
}