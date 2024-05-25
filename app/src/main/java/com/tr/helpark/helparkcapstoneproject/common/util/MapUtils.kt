package com.tr.helpark.helparkcapstoneproject.common.util

import com.airbnb.lottie.model.Marker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow


object MapUtils {

    val markers = mutableListOf<Marker>()

    val markerLocation: MutableSharedFlow<LatLng> = MutableSharedFlow(replay = 1)

    var lastClickedMarkerId = 0
}