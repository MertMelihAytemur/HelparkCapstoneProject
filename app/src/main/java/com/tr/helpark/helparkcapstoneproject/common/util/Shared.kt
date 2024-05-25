package com.tr.helpark.helparkcapstoneproject.common.util

import java.util.Calendar

fun calculateDensity(current: Double, total: Double): Double {
    return (current / total) * 100
}

fun isDayTime(): Boolean {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    val isDaytime = hourOfDay in 6..17 // 6 AM to 5 PM

    return isDaytime
}