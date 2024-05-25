package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType

fun Fragment.navigateWithAnimation(
    @IdRes destinationId: Int,
    bundle : Bundle? = null,
    @AnimRes enterAnim: Int = R.anim.slide_in_right,
    @AnimRes exitAnim: Int = R.anim.slide_out_left,
    @AnimRes popEnterAnim: Int = R.anim.slide_in_left,
    @AnimRes popExitAnim: Int = R.anim.slide_out_right
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()
    findNavController().navigate(destinationId, bundle, navOptions)
}


fun Fragment.showToastMessage(
    message: CharSequence,
    xOffset: Int = 0,
    yOffset: Int = 100,
    toastType: ToastMessageType,
) {
    val inflater = layoutInflater
    val layout = inflater.inflate(R.layout.custom_toast_message_view, null)
    val toastText = layout.findViewById<TextView>(R.id.tvToastMessage)
    val icon = layout.findViewById<ImageView>(R.id.ivToastStatus)
    toastText.text = message

    val toast = Toast(context)
    toast.setGravity(Gravity.BOTTOM, xOffset, yOffset)
    toast.duration = Toast.LENGTH_LONG
    toast.view = layout

    when (toastType) {
        ToastMessageType.GENERAL_SUCCESS -> {
            icon.setImageResource(R.drawable.ic_toast_message_success)
        }

        ToastMessageType.GENERAL_ERROR -> {
            icon.setImageResource(R.drawable.ic_toast_message_failure)
        }

        else -> {}
    }

    toast.show()
}