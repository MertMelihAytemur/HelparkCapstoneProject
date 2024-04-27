package com.tr.helpark.helparkcapstoneproject.common.util

import com.google.android.gms.maps.model.LatLng

object Constants {
    const val HELPARK_WEB_SITE_URL= "https://helpark.com.tr/privacy-policies"
    const val BASE_URL = "https://api.ibb.gov.tr/ispark/"
    const val VIEW_TRANSLATION_DURATION = 300L
    const val PRIVACY_POLICY = "https://helpark.com.tr/privacy-policies/"

    val MALTEPE_LAT_LNG = LatLng(40.935812511703745, 29.16035006866268)
    val USKUDAR_LAT_LNG = LatLng(41.01504041329504, 29.065238375745697)
    val BESIKTAS_LAT_LNG = LatLng(41.064723503892594, 29.021816481045544)
    val KADIKOY_LAT_LNG = LatLng(40.980334508903454, 29.0824845402493)


    var isDay : Boolean = false

    const val NETWORK_CHECK_INTERVAL = 5000L

    const val STORE_URL = "https://play.google.com/store/apps/details?id=com.helpark.helpark"
    const val GOOGLE_MAPS_BASE_URL = "http://maps.google.com/maps?daddr="
}