package com.hdk24.nominatim.utils

import android.content.Context
import android.net.*
import android.os.Build

/*
 *  Created by Hanantadk on 27/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 27/07/20.
 */
object NetworkUtils {

    fun isConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isConnected = false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            isConnected = activeNetwork?.isConnectedOrConnecting == true
        } else {
            ConnectivityManager.NetworkCallback()
            val networkRequest =
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
            cm.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        isConnected = true
                    }
                })
        }

        return isConnected
    }
}