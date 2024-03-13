package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan

fun String.formatPhoneNumber(): String {
    val digits = replace("\\D+".toRegex(), "")
    return if (digits.startsWith("0")) {
        digits.substring(1)
    } else {
        digits
    }
}

fun String.boldNumbersAndAsterisks(): SpannableString {
    val spannableString = SpannableString(this)

    val regex = Regex("[0-9*]+")
    val matches = regex.findAll(this)
    for (match in matches) {
        val startIndex = match.range.first
        val endIndex = match.range.last + 1
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}