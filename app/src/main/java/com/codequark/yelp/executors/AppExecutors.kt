package com.codequark.yelp.executors

import androidx.annotation.NonNull
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {
    companion object {
        private const val NUMBER_OF_THREADS = 4

        @NonNull
        private val mainThread: Executor = MainThreadExecutor()

        @NonNull
        private val ioThread: Executor = Executors.newSingleThreadExecutor()

        @NonNull
        private val networkThread: Executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @NonNull
        fun mainThread(): Executor {
            return mainThread
        }

        @NonNull
        fun ioThread(): Executor {
            return ioThread
        }

        @NonNull
        fun networkThread(): Executor {
            return networkThread
        }
    }
}

fun mainThread(@NonNull function: () -> Unit) {
    AppExecutors.mainThread().execute(function)
}

fun ioThread(@NonNull function: () -> Unit) {
    AppExecutors.ioThread().execute(function)
}

fun networkThread(@NonNull function: () -> Unit) {
    AppExecutors.networkThread().execute(function)
}