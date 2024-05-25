package com.tr.helpark.helparkcapstoneproject.features.search.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlacePredictionModel(
    val id: String?,
    val name: String?,
    val address: String?,
    val latLng : LatLng?,
    var isHistory : Boolean = false
): Parcelable