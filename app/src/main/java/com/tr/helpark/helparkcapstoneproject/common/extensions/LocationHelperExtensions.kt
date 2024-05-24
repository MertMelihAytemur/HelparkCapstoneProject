package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
fun requestTurnOnLocationServices(
    context: Activity,
    onResponseSuccess: () -> Unit,
    onResponseFailed: () -> Unit
) {
    val request = LocationRequest.create()
        .setInterval(60000)
        .setFastestInterval(5000)

    val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
    builder.setAlwaysShow(true)
    val task = LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())

    task
        .addOnCompleteListener {
            try {
                it.getResult(ApiException::class.java)
                val mLocationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            if (location != null) {
                                onResponseSuccess()
                                return
                            }
                        }
                        onResponseFailed()
                    }
                }
                LocationServices.getFusedLocationProviderClient(context)
                    .requestLocationUpdates(request, mLocationCallback, null)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable =
                                exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                context,
                                LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            //Timber.e(e)
                        } catch (e: ClassCastException) {
                            //Timber.e(e)
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        onResponseFailed()
                    }
                }
            }
        }
}

/**
 * Get District Name for LatLng
 */


/**
 * Return distance between two locations
 */

fun getDistanceInKm(currentLocation : LatLng, destinationLocation : LatLng): Float {
    val location1 = Location("")
    location1.latitude = currentLocation.latitude
    location1.longitude = currentLocation.longitude

    val location2 = Location("")
    location2.latitude = destinationLocation.latitude
    location2.longitude = destinationLocation.longitude

    val distanceInMeters = FloatArray(1)
    Location.distanceBetween(
        location1.latitude, location1.longitude,
        location2.latitude, location2.longitude,
        distanceInMeters
    )

    // Convert meters to kilometers and return the result
    return distanceInMeters[0] / 1000
}