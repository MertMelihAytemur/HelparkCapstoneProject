package com.tr.helpark.helparkcapstoneproject.common.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object UiConstants {

    /**
     * Configures the app to draw under status bar
     * if necessary. For now, set as API 23
     * and above since navigation bar flags were
     * officially introduced in Android 6.
     */
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    val DRAW_UNDER_STATUS_BAR: Boolean = true

    /**
     * Configures the app to draw under navigation bar
     * if necessary. For now, set as API 27
     * and above since navigation bar flags were
     * officially introduced in Android 8.1.
     */
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    val DRAW_UNDER_NAVIGATION_BAR: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
}