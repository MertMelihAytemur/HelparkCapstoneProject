package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.android.gms.maps.model.LatLng
import com.tr.helpark.helparkcapstoneproject.common.util.Constants.GOOGLE_MAPS_BASE_URL

fun Context.redirectUserToGoogleMaps(direction : LatLng){
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("$GOOGLE_MAPS_BASE_URL${direction.latitude},${direction.longitude}")
    )
    startActivity(intent)
}

fun Context.createShareLink(direction : LatLng) : String{
    return "$GOOGLE_MAPS_BASE_URL${direction.latitude},${direction.longitude}"
}
fun Context.openApplicationDetailSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", this@openApplicationDetailSettings.packageName, null)
        }
    )
}