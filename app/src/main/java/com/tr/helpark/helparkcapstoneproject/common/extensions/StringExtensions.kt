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

fun String.formatAndInsertPhoneNumber(phoneNumber: String): String {
    // Ensure the phone number is at least 4 characters long
    if (phoneNumber.length < 4) return this

    // Hide all but the last 4 digits of the phone number
    val maskedNumber = phoneNumber.takeLast(4).padLeft(phoneNumber.length, '*')

    // Replace the %s in the string with the masked number
    return this.replace("%s", maskedNumber)
}

// Function to pad the beginning of a String with a specific character until a desired length is reached
fun String.padLeft(length: Int, char: Char): String {
    return char.toString().repeat(length - this.length) + this
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    return this.matches(emailRegex.toRegex())
}

fun String.toPhoneNumberFormat(): String {
    val cleaned = this.filter { it.isDigit() }

    return if (cleaned.length == 10) {
        "+90 ${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6, 8)} ${cleaned.substring(8, 10)}"
    } else {
        this
    }
}

fun String.addSpacesBetweenLettersAndDigits(): String {
    val stringBuilder = StringBuilder()
    for (i in this.indices) {
        val currentChar = this[i]
        if (i > 0) {
            val previousChar = this[i - 1]
            if (currentChar.isDigit() && previousChar.isLetter() || currentChar.isLetter() && previousChar.isDigit()) {
                stringBuilder.append(' ')
            }
        }
        stringBuilder.append(currentChar)
    }
    return stringBuilder.toString()
}

fun String.toMaskedCardNumber(): String {
    if (this.length != 16) return this // Eğer gelen string 16 karakter değilse, olduğu gibi döndür.

    val firstPart = this.substring(0, 4)
    val maskedPart = "**** ****"
    val lastPart = this.substring(12, 16)

    return "$firstPart $maskedPart $lastPart"
}