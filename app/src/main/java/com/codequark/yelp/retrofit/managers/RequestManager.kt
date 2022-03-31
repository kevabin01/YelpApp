package com.codequark.yelp.retrofit.managers

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import com.codequark.yelp.retrofit.interceptor.RetrofitInterceptor
import com.codequark.yelp.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@SuppressLint("CustomX509TrustManager")
abstract class RequestManager<Result>(
    @NonNull
    val TAG: String,

    @NonNull
    baseUrl: String,

    @NonNull
    timeout: Long,

    @NonNull
    headers: HashMap<String, String>
) {
    @NonNull
    protected val retrofit: Retrofit

    init {
        LogUtils.print("RequestName", TAG + "Request")

        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val interceptor: Interceptor = RetrofitInterceptor(TAG, headers)

        okHttpBuilder.addInterceptor(interceptor)
        okHttpBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
        okHttpBuilder.callTimeout(timeout, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(timeout, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(timeout, TimeUnit.SECONDS)

        val okHttpClient: OkHttpClient = okHttpBuilder.build()

        val builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.client(okHttpClient)
        builder.addConverterFactory(GsonConverterFactory.create())

        this.retrofit = builder.build()
    }

    protected abstract suspend fun onPreExecute(): Boolean
    protected abstract suspend fun onExecute(@NonNull params: HashMap<String, Any>): Result?
    protected abstract suspend fun onPostExecute(result: Result?)

    suspend fun execute(@NonNull params: HashMap<String, Any>) {
        val networkAvailable = withContext(Dispatchers.Main) {
            onPreExecute()
        }

        if(networkAvailable) {
            val result = withContext(Dispatchers.IO) {
                onExecute(params) // runs in background thread without blocking the Main Thread
            }

            withContext(Dispatchers.Main) {
                onPostExecute(result) // runs in Main Thread
            }
        }
    }
}