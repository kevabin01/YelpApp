package com.codequark.yelp.managers

import androidx.annotation.IntDef

class NetworkManager {
    companion object {
        private var connected: Boolean = false

        fun setConnected(connected: Boolean) {
            this.connected = connected
        }

        fun isNetworkConnected(): Boolean {
            return connected
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(NetworkStateDef.DEFAULT, NetworkStateDef.CONNECTED, NetworkStateDef.DISCONNECTED)
    annotation class NetworkStateDef {
        companion object {
            const val DEFAULT = 0
            const val CONNECTED = 1
            const val DISCONNECTED = 2
        }
    }
}