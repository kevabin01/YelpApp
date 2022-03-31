package com.codequark.yelp.interfaces

interface NetworkListener {
    fun onConnected() {}
    fun onDisconnected() {}
}