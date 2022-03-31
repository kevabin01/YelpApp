package com.codequark.yelp.utils

import androidx.annotation.NonNull
import okio.Buffer
import java.io.EOFException

class RequestUtils {
    companion object {
        const val request = "Request"

        fun isPlaintext(@NonNull buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = if(buffer.size < 64) buffer.size else 64
                buffer.copyTo(prefix, 0, byteCount)

                for(i in 0..15) {
                    if(prefix.exhausted()) {
                        break
                    }

                    val codePoint = prefix.readUtf8CodePoint()

                    if(Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }

                true
            } catch (ex: EOFException) {
                false // Truncated UTF-8 sequence.
            }
        }
    }
}