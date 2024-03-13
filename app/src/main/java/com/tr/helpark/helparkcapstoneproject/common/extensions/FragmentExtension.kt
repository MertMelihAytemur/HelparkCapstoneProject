package com.tr.helpark.helparkcapstoneproject.common.extensions

import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R

fun Fragment.navigateWithAnimation(
    direction: NavDirections,
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
    findNavController().navigate(direction, navOptions)
}