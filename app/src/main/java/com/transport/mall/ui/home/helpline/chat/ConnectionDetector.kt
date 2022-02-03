package com.transport.mall.ui.home.helpline.chat

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionDetector(var _context: Context) {
    val isConnectingToInternet: Boolean
        get() {
            val connectivity = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null) for (networkInfo in info) if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }
}