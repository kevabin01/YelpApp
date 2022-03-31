package com.codequark.yelp.retrofit.interceptor

import androidx.annotation.NonNull
import com.codequark.yelp.managers.WriteManager
import com.codequark.yelp.utils.Utils
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.utils.RequestUtils
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import java.io.IOException
import java.net.SocketTimeoutException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class RetrofitInterceptor(
    @NonNull
    val TAG: String,

    @NonNull
    val headers: HashMap<String, String>
): Interceptor {
    constructor(@NonNull TAG: String): this(TAG, HashMap<String, String>())

    companion object {
        private val UTF8: Charset = StandardCharsets.UTF_8
    }

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Proceed Request
        val oldRequest = chain.request()
        val requestBody = oldRequest.body

        val startTimeNs = System.nanoTime()

        try {
            WriteManager.init(TAG + RequestUtils.request)

            val buffer = Buffer()
            requestBody?.writeTo(buffer)

            val connection: Connection? = chain.connection()
            val method: String = oldRequest.method
            val host: String = oldRequest.url.host
            val query: String = oldRequest.url.query ?: "No Query"
            val scheme: String = oldRequest.url.scheme
            val url: String = oldRequest.url.toString()
            val protocol: String = connection?.protocol()?.toString() ?: Protocol.HTTP_1_1.toString()

            val requestContentType: String = requestBody?.contentType()?.toString() ?: "Empty Content-Type"
            val requestContentLength: String = requestBody?.contentLength()?.toString() ?: "Empty Content-Length"
            val requestContentBody: String = if(RequestUtils.isPlaintext(buffer)) buffer.readString(UTF8) else "Empty Body"

            WriteManager.writeLog("Method: $method")
            WriteManager.writeLog("Host: $host")
            WriteManager.writeLog("Query: $query")
            WriteManager.writeLog("Scheme: $scheme")
            WriteManager.writeLog("URL: $url")
            WriteManager.writeLog("Protocol: $protocol")

            WriteManager.writeTitle("<--- Request --->")

            WriteManager.writeLog("Content-Type: $requestContentType")
            WriteManager.writeLog("Content-Length: $requestContentLength")
            if(requestContentBody.isNotEmpty()) {
                WriteManager.writeLog("Body: $requestContentBody")
            } else {
                WriteManager.writeLog("Body: Empty Body")
            }
        } catch (ex: IOException) {
            LogUtils.print("Exception", ex.toString())
        }

        val requestBuilder: Request.Builder = oldRequest.newBuilder()
            .method(oldRequest.method, oldRequest.body)

        if(headers.isEmpty()) {
            WriteManager.writeLog("Headers: Empty Headers")
        } else {
            for(header in headers) {
                requestBuilder.header(header.key, header.value)

                WriteManager.writeLog("Header: ${header.key}: ${header.value}")
            }
        }

        WriteManager.writeTitle("<--- Request --->")

        // Proceed Response
        val response: Response = try {
            chain.proceed(requestBuilder.build())
        } catch (ex: Exception) {
            when(ex) {
                is SocketTimeoutException -> {
                    WriteManager.writeLog("SocketTimeoutException: $ex")
                    throw SocketTimeoutException(ex.toString())
                }

                is RuntimeException -> {
                    WriteManager.writeLog("RuntimeException: $ex")
                    throw RuntimeException(ex.toString())
                }

                is IOException -> {
                    WriteManager.writeLog("IOException: $ex")
                    throw IOException(ex.toString())
                }

                else -> {
                    WriteManager.writeLog("Exception: $ex")
                    throw Exception(ex.toString())
                }
            }
        }

        val endTimeNs = System.nanoTime()
        val tookTimeMs = TimeUnit.NANOSECONDS.toMillis(endTimeNs - startTimeNs)

        val startTimeNsStr = Utils.getDateTimeFormatted(startTimeNs)
        val endTimeNsStr = Utils.getDateTimeFormatted(endTimeNs)

        WriteManager.writeLog("StartTime: $startTimeNsStr ($startTimeNs)")
        WriteManager.writeLog("EndTime: $endTimeNsStr ($endTimeNs)")
        WriteManager.writeLog("Executed in: $tookTimeMs ms")

        WriteManager.writeTitle("<--- Response --->")

        val responseBody = response.body
        val source: BufferedSource? = responseBody?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer

        val responseContentType: String = responseBody?.contentType()?.toString() ?: "Empty Content-Type"
        val responseContentLength: String = responseBody?.contentLength()?.toString() ?: "Empty Content-Length"
        val responseContentBody: String = if(buffer != null) {
            if(RequestUtils.isPlaintext(buffer)) {
                buffer.clone().readString(UTF8)
            } else {
                "Empty Body"
            }
        } else {
            "Empty Body"
        }

        if(response.isSuccessful) {
            WriteManager.writeLog("ResponseCode: ${response.code}")
        }

        WriteManager.writeLog("Content-Type: $responseContentType")
        WriteManager.writeLog("Content-Length: $responseContentLength")
        WriteManager.writeLog("Body: $responseContentBody")

        val responseHeaders: Headers = response.headers

        if(responseHeaders.size == 0) {
            WriteManager.writeLog("Headers: Empty Headers")
        } else {
            for(header in responseHeaders) {
                WriteManager.writeLog("Header: ${header.first}: ${header.second}")
            }
        }

        WriteManager.writeTitle("<--- Response --->")

        return response
    }
}