package com.tr.helpark.helparkcapstoneproject.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NetworkHelper {

    /**
     * Returns whether the device is connected
     * to the internet at that moment or not.
     */
    suspend fun hasInternetConnection(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext runCatching {
                val service = ContextCompat.getSystemService(
                    context,
                    ConnectivityManager::class.java
                )
                val activeNetwork = service?.activeNetwork
                activeNetwork ?: return@runCatching false

                val capabilities = service.getNetworkCapabilities(activeNetwork)
                capabilities ?: return@runCatching false

                return@runCatching capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }.getOrNull() ?: return@withContext false
        }
    }

    fun hasInternetConnectionNow(context: Context): Boolean{
        return runCatching {
            val service = ContextCompat.getSystemService(
                context,
                ConnectivityManager::class.java
            )
            val activeNetwork = service?.activeNetwork
            activeNetwork ?: return@runCatching false

            val capabilities = service.getNetworkCapabilities(activeNetwork)
            capabilities ?: return@runCatching false

            return@runCatching capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }.getOrNull() ?: return false
    }
}