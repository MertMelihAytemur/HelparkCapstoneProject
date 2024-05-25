package com.tr.helpark.helparkcapstoneproject.common.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    var userLocation: LatLng? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun getUserLocation(
        onLocationSaved: (LatLng) -> Unit,
        onResponseFailed: () -> Unit = {}
    ) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                userLocation = LatLng(location.latitude, location.longitude)
                userLocation?.let {
                    onLocationSaved(it)
                }
            } ?: run(onResponseFailed)

        }.addOnFailureListener {
            onResponseFailed()
        }
    }
}
