package com.codequark.yelp.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.codequark.yelp.broadcasts.NetworkStateReceiver
import com.codequark.yelp.interfaces.NetworkListener
import com.codequark.yelp.managers.NetworkManager
import com.codequark.yelp.managers.NetworkManager.NetworkStateDef
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.SecureLiveData

@Suppress("deprecation")
@SuppressLint("ObsoleteSdkInt")
class NetworkRepository private constructor(@NonNull context: Context) {
    @NonNull
    private val network = SecureLiveData(NetworkStateDef.DEFAULT)

    @NonNull
    private val networkCallback: ConnectivityManager.NetworkCallback = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            LogUtils.print("onConnected N")

            NetworkManager.setConnected(true)
            setNetwork(NetworkStateDef.CONNECTED)
        }

        override fun onLost(network: Network) {
            LogUtils.print("onDisconnected N")

            NetworkManager.setConnected(false)
            setNetwork(NetworkStateDef.DISCONNECTED)
        }
    }

    companion object {
        @Volatile
        private var instance: NetworkRepository? = null

        fun getInstance(@NonNull context: Context): NetworkRepository = instance ?: synchronized(this) {
            instance ?: NetworkRepository(context).also {
                instance = it
            }
        }
    }

    init {
        registerNetwork(context)
    }

    @NonNull
    fun getNetworkState(): LiveData<Int> {
        return network
    }

    fun setNetwork(@NetworkStateDef network: Int) {
        this.network.postValue(network)
    }

    private fun registerNetwork(@NonNull context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogUtils.print("register registerNetwork Android > Nougat")

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            LogUtils.print("register registerNetwork Android < MarshMallow")

            val networkStateReceiver = NetworkStateReceiver(context, object: NetworkListener {
                override fun onConnected() {
                    LogUtils.print("onConnected M")
                    NetworkManager.setConnected(true)
                    setNetwork(NetworkStateDef.CONNECTED)
                }

                override fun onDisconnected() {
                    LogUtils.print("onDisconnected M")
                    NetworkManager.setConnected(false)
                    setNetwork(NetworkStateDef.DISCONNECTED)
                }
            })

            networkStateReceiver.init()
            context.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    private fun unregisterNetwork(@NonNull context: Context) {
        LogUtils.print("register unregisterNetwork")

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}