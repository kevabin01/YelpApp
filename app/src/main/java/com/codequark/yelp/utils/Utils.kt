package com.codequark.yelp.utils

import androidx.annotation.NonNull
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        @NonNull
        fun getTimestamp(): Long {
            return System.currentTimeMillis()
        }

        @NonNull
        fun getDateTimeFormatted(@NonNull timestamp: Long): String {
            val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
            return format.format(Date(timestamp))
        }
    }
}