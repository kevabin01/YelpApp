package com.codequark.yelp.retrofit.requests

import androidx.annotation.NonNull
import com.codequark.yelp.retrofit.Endpoints
import com.codequark.yelp.retrofit.base.RetrofitRequest
import com.codequark.yelp.retrofit.models.Result
import com.codequark.yelp.retrofit.services.SearchService
import com.codequark.yelp.utils.Constants.JsonConstants
import java.io.IOException

class SearchRequest(
    @NonNull
    headers: HashMap<String, String>
): RetrofitRequest(
    "Search",
    Endpoints.baseUrl,
    headers
) {
    override suspend fun onExecute(@NonNull params: HashMap<String, Any>): Result? {
        try {
            val query: String = params[JsonConstants.query] as String
            val latitude: Double = params[JsonConstants.latitude] as Double
            val longitude: Double = params[JsonConstants.longitude] as Double

            val service: SearchService = retrofit.create(SearchService::class.java)
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = service.request(query, latitude, longitude)

            if(response.isSuccessful) {
                val model = response.body()

                if(model != null) {
                    try {
                        return model
                    } catch (ex: Exception) {
                        onError(ex)
                    }
                }
            } else {
                onError(RuntimeException("Error on Execution"))
            }
        } catch (ex: IOException) {
            onError(ex)
        }

        return null
    }
}