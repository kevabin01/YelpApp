package com.codequark.yelp.utils

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.codequark.yelp.BuildConfig

class LogUtils {
    companion object {
        fun print() {

        }

        fun print(@Nullable text: String?) {
            if(BuildConfig.DEBUG) {
                if(text == null) {
                    return
                }

                if(text.isEmpty()) {
                    return
                }

                Log.d("Kevin", text)
            }
        }

        fun print(@NonNull TAG: String, @Nullable text: String?) {
            if(BuildConfig.DEBUG) {
                if(TAG.isEmpty()) {
                    return
                }

                if(text == null) {
                    return
                }

                if(text.isEmpty()) {
                    return
                }

                Log.d(TAG, text)
            }
        }
    }
}