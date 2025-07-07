package com.luisptapia.pickupandroid.utils

import android.app.Application
import android.net.*
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(private val connectivityManager: ConnectivityManager)
    : LiveData<Boolean>() {

    constructor(application: Application) : this(
        application.getSystemService(ConnectivityManager::class.java)
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }

        override fun onUnavailable() {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
