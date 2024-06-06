package com.tr.helpark.helparkcapstoneproject.common.util

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import tr.com.helpark.core.util.logD

/**
 *Created by Mert Melih Aytemur on 2/10/2023.
 */
class MapManager private constructor(
    private val context: Context,
    private val locationHelper: LocationHelper,
    private val map: GoogleMap
) {

    private var currentZoom = 13F

    var radius: Double = 2000.0
    fun setNearestMaviShops(stores: ArrayList<GetAllParksUiModelItem>) {
        stores.forEach { store ->

            val markerOptions = MarkerOptions()
            val placeName = store.parkName
            val latLng = LatLng(
                store.lat?.toDouble() ?: 0.0,
                store.lng?.toDouble() ?: 0.0
            )

            markerOptions.position(latLng)
            markerOptions.title(placeName)
            markerOptions.snippet(store.district)
            markerOptions.icon(
                getMarkerIcon("#000000")
            )

            map.addMarker(markerOptions)?.apply {
                tag = stores.indexOf(store)
            }
        }
    }

    private fun getMarkerIcon(color: String): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    /**
     * animate camera to user location one time
     */
    fun getUserLocationAndMoveCamera(
        onLocationSaved: (LatLng) -> Unit,
        onLocationFailed: () -> Unit
    ) {
        locationHelper.getUserLocation(onLocationSaved = {
            addMarkerToUserLocation(it)
            onLocationSaved(it)
        }, onResponseFailed = {
            onLocationFailed()
        })
    }

    fun moveCameraToLocation(userPosition: LatLng) {
        locationHelper.userLocation = userPosition
        addMarkerToUserLocation(userPosition)
        currentZoom = 12F
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition, currentZoom))
    }

    fun moveCameraToLocation(userPosition: LatLng, cameraPosition: CameraPosition) {
        locationHelper.userLocation = userPosition
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun setIsCompassEnabled(isEnabled: Boolean) {
        map.uiSettings.isCompassEnabled = isEnabled
    }

    fun setMinZoomPreference(minZoomPreference: Float) {
        map.setMinZoomPreference(minZoomPreference)
    }

    fun setMaxZoomPreference(maxZoomPreference: Float) {
        map.setMaxZoomPreference(maxZoomPreference)
    }

    fun setOnCameraMoveStartedListener(onCameraMoveStartedListener: GoogleMap.OnCameraMoveStartedListener) {
        map.setOnCameraMoveStartedListener(onCameraMoveStartedListener)
    }

    fun setOnCameraIdleListener(onCameraIdleListener: GoogleMap.OnCameraIdleListener) {
        currentZoom = map.cameraPosition.zoom
        map.setOnCameraIdleListener(onCameraIdleListener)
    }

    fun setCurrentZoomToCameraPosition() {
        currentZoom = map.cameraPosition.zoom
    }

    fun setMapUiSettingsForLoading(isLoading: Boolean) {
        map.uiSettings.apply {
            isCompassEnabled = !isLoading
            isMapToolbarEnabled = isLoading
            setAllGesturesEnabled(!isLoading)
        }
    }

    fun onInfoWindowClick(action: (Int) -> Unit) {
        map.setOnInfoWindowClickListener {
            runCatching {
                action(it.tag as Int)
            }.onFailure {
                logD("onInfoWindowClick: ${it.message}")
            }
        }
    }

    /**
     * Add markers to [latLng]
     */
    private fun addMarkerToUserLocation(latLng: LatLng) {
        map.clear()
        drawCircle(radius, latLng)
        map.addMarker(
            MarkerOptions().position(latLng).title(
                context.getString(R.string.current_location_marker_title)
            ).icon(
                getMarkerIcon("#4395a1")
            )
        )?.apply {
            tag = -1
        }?.showInfoWindow()
    }

    private fun drawCircle(radius: Double, center: LatLng) {
        val circleOptions = CircleOptions()
            .center(center)
            .radius(radius) // Radius in meters (4 km)
            .strokeWidth(3f)
            .strokeColor(Color.parseColor("#1E90FF")) // Standard blue for stroke
            // Lighter blue for fill, with 70 alpha for transparency
            .fillColor(Color.argb(70, 135, 206, 250)) // Sky Blue color

        map.addCircle(circleOptions)
    }

    class MapManagerFactory {
        companion object {
            fun create(
                context: Context,
                map: GoogleMap, locationHelper: LocationHelper
            ): MapManager {
                return MapManager(context, locationHelper, map)
            }
        }
    }
}