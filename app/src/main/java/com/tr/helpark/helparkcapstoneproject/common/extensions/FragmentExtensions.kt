package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tr.helpark.helparkcapstoneproject.R

/**
 *Created by Mert Melih Aytemur on 1/19/2024.
 */

fun Fragment.showToastMessage(message: CharSequence, xOffset: Int = 0, yOffset: Int = 0) {
    val inflater = layoutInflater
    val layout = inflater.inflate(R.layout.custom_toast_message_view, null)
    val toastText = layout.findViewById<TextView>(R.id.tvToastMessage)
    toastText.text = message
    val toast = Toast(context)
    toast.setGravity(Gravity.TOP, xOffset, yOffset)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}