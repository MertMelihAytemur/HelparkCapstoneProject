package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.tr.helpark.helparkcapstoneproject.common.customview.CustomToastMessageView
import com.tr.helpark.helparkcapstoneproject.common.util.Constants

/**
 *Created by Mert Melih Aytemur on 1/19/2024.
 */

fun View.gone() {
    this.visibility = View.GONE
}
fun View.visible() {
    this.visibility = View.VISIBLE
}
fun View.hide() {
    this.visibility = View.INVISIBLE
}
fun CustomToastMessageView.showCustomMessage(
    message: String,
    isPositive: Boolean = true,
    timeMillis : Long = 2000L
) {
    this.apply {
        setToastStatus(isPositive)
        setToastMessageText(message)
        show(timeMillis)
    }
}

fun View.getChildAt(index: Int): View? {
    return if (this is ViewGroup)
        getChildAt(index)
    else
        null
}

fun getMarkerIcon(color: String): BitmapDescriptor {
    val hsv = FloatArray(3)
    Color.colorToHSV(Color.parseColor(color), hsv)
    return BitmapDescriptorFactory.defaultMarker(hsv[0])
}

fun handleViewVisibilityWithTranslationXEnd(vararg views: View, show: Boolean) {
    views.forEach { view ->
        if (show)
            view.animate().translationX(0.0F).duration = Constants.VIEW_TRANSLATION_DURATION
        else
            view.animate().translationX(view.width.toFloat() + view.marginEnd).duration = Constants.VIEW_TRANSLATION_DURATION

    }
}

fun handleViewVisibilityWithTranslationYTop(vararg views: View, show: Boolean) {
    views.forEach { view ->
        if (show)
            view.animate().translationY(0.0F).duration = Constants.VIEW_TRANSLATION_DURATION
        else
            view.animate().translationY(-view.height.toFloat() - view.marginTop).duration = Constants.VIEW_TRANSLATION_DURATION
    }
}

fun View.animateAlpha(show: Boolean) {
    this.animate().alpha(if (show) 1.0f else 0.0f).setDuration(Constants.VIEW_TRANSLATION_DURATION).start()
}