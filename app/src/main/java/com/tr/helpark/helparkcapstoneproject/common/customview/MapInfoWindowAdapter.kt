package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.tr.helpark.helparkcapstoneproject.R

class MapInfoWindowAdapter(
    private val context : Context
) : GoogleMap.InfoWindowAdapter {
    var mWindow: View = LayoutInflater.from(context).inflate(R.layout.layout_marker_info_window, null)

    private fun setInfoWindowText(marker: Marker) {
        val title = marker.title
        val tvTitle = mWindow.findViewById<TextView>(R.id.tvTitle)
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
    }

    override fun getInfoContents(p0: Marker): View {
        setInfoWindowText(p0)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker): View {
        setInfoWindowText(p0)
        return mWindow
    }

}