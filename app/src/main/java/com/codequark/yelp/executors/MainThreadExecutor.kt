package com.codequark.yelp.executors

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import java.util.concurrent.Executor

class MainThreadExecutor: Executor {
    @NonNull
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun execute(@NonNull command: Runnable) {
        mainThreadHandler.post(command)
    }
}