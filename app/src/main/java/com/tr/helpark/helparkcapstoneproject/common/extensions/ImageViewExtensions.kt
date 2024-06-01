package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.widget.ImageView
import com.tr.helpark.helparkcapstoneproject.R

fun ImageView.setCarParkSavedStatus(isSaved: Boolean) {
    val imageRes = if (isSaved) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
    this.setImageResource(imageRes)
}