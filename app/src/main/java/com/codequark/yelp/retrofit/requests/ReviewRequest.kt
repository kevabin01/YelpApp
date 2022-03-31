package com.codequark.yelp.retrofit.requests

import androidx.annotation.NonNull
import com.codequark.yelp.retrofit.Endpoints
import com.codequark.yelp.retrofit.base.RetrofitRequest
import com.codequark.yelp.retrofit.models.Result
import com.codequark.yelp.retrofit.services.ReviewService
import com.codequark.yelp.utils.Constants.JsonConstants
import java.io.IOException

class ReviewRequest(
    @NonNull
    headers: HashMap<String, String>
): RetrofitRequest(
    "Review",
    Endpoints.baseUrl,
    headers
) {
    override suspend fun onExecute(@NonNull params: HashMap<String, Any>): Result? {
        try {
            val alias: String = params[JsonConstants.alias] as String

            val service: ReviewService = retrofit.create(ReviewService::class.java)
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = service.request(alias)

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