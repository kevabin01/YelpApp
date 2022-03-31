package com.codequark.yelp.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.annotation.NonNull
import com.codequark.yelp.interfaces.NetworkListener
import com.codequark.yelp.utils.LogUtils

@Suppress("deprecation")
class NetworkStateReceiver(
    @NonNull
    private val context: Context,

    @NonNull
    private val listener: NetworkListener
): BroadcastReceiver() {
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var connected: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null || intent.extras == null) {
            return
        }

        if(checkStateChanged()) {
            notifyState()
        }
    }

    fun init() {
        notifyState()
    }

    private fun checkStateChanged(): Boolean {
        LogUtils.print("checkStateChanged")

        val previewState = connected
        val activeNetwork = connectivityManager.activeNetworkInfo

        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        return previewState != connected
    }

    private fun notifyState() {
        LogUtils.print("notifyState")

        if(connected) {
            listener.onConnected()
        } else {
            listener.onDisconnected()
        }
    }

    init {
        notifyState()

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        context.registerReceiver(this, intentFilter)
        checkStateChanged()
    }
}