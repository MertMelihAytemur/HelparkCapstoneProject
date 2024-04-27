package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.view.View
import android.view.ViewGroup
import com.tr.helpark.helparkcapstoneproject.common.customview.CustomToastMessageView

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