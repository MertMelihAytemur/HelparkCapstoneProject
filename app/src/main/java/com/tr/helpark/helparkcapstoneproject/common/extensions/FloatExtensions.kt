package com.tr.helpark.helparkcapstoneproject.common.extensions

fun Float.toCurrencyString(): String {
    return String.format("%.2f ₺", this)
}