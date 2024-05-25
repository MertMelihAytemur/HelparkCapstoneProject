package com.tr.helpark.helparkcapstoneproject.common.util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem


interface MapActions {

    fun moveCamera(coordinate: LatLng, cameraPosition: CameraPosition)

    fun setParksToMap(parkList: List<GetAllParksUiModelItem>)
}