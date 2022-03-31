package com.codequark.yelp.retrofit.base

import androidx.annotation.NonNull
import com.codequark.yelp.interfaces.ErrorListener
import com.codequark.yelp.interfaces.NetworkListener
import com.codequark.yelp.interfaces.SuccessListener
import com.codequark.yelp.managers.NetworkManager
import com.codequark.yelp.retrofit.managers.RequestManager
import com.codequark.yelp.retrofit.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class RetrofitRequest(
    @NonNull
    TAG: String,

    @NonNull
    baseUrl: String,

    @NonNull
    headers: HashMap<String, String>
): RequestManager<Result>(TAG, baseUrl, 15L, headers) {
    private var successListener: SuccessListener<Result>? = null

    private var errorListener: ErrorListener? = null

    private var networkListener: NetworkListener? = null

    open fun setSuccessListener(@NonNull successListener: SuccessListener<Result>) {
        this.successListener = successListener
    }

    open fun setErrorListener(@NonNull errorListener: ErrorListener) {
        this.errorListener = errorListener
    }

    open fun setNetworkListener(@NonNull networkListener: NetworkListener) {
        this.networkListener = networkListener
    }

    suspend fun onSuccess(@NonNull result: Result) {
        withContext(Dispatchers.Main) {
            successListener?.onSuccess(result)
        }
    }

    suspend fun onError(@NonNull ex: Exception) {
        withContext(Dispatchers.Main) {
            errorListener?.onError(ex)
        }
    }

    suspend fun onConnected() {
        withContext(Dispatchers.Main) {
            networkListener?.onConnected()
        }
    }

    suspend fun onDisconnected() {
        withContext(Dispatchers.Main) {
            networkListener?.onDisconnected()
        }
    }

    suspend fun sendError(@NonNull response: Response<Result>) {
        val content: String = if(response.body() != null) {
            "Code: " + response.code() + " - Response: " + response.body()
        } else {
            "Code: " + response.code()
        }

        onError(RuntimeException(content))
    }

    override suspend fun onPreExecute(): Boolean {
        return if(NetworkManager.isNetworkConnected()) {
            onConnected()
            true
        } else {
            onDisconnected()
            false
        }
    }

    override suspend fun onPostExecute(result: Result?) {
        if(result != null) {
            onSuccess(result)
        }
    }
}