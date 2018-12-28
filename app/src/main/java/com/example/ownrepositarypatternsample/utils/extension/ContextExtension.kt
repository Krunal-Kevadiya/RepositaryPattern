package com.example.ownrepositarypatternsample.utils.extension

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

@Suppress("unchecked_cast")
fun <T> Context.getService(name: String): T? = getSystemService(name) as? T

val Context.connectivityManager: ConnectivityManager?
    get() = getService(Context.CONNECTIVITY_SERVICE)

@get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
val Context.hasConnection: Boolean
    get() = connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false